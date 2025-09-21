package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer.UniqueViolated;
import xyz.arinmandri.playground.core.member.MKeyBasic;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberSer;
import xyz.arinmandri.playground.core.member.Z_MKeyBasicAdd;
import xyz.arinmandri.playground.core.member.Z_MemberAdd;
import xyz.arinmandri.playground.core.member.Z_MemberEdit;
import xyz.arinmandri.playground.security.user.User;
import xyz.arinmandri.playground.security.user.UserGuest;
import xyz.arinmandri.playground.security.user.UserNormal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping( "/member" )
@RequiredArgsConstructor
public class ApiMember extends ApiA
{

	final MemberSer memberSer;

	final private PasswordEncoder pwEncoder;

	@GetMapping( "/whoami" )
	public ResponseEntity<apiWhoamiRes> apiWhoami (
	        @AuthenticationPrincipal User u ) {

		String type = u.getType().toString();
		String nick;
		String propic = null;
		switch( u ){
		case UserNormal un -> {
			Member m = getMemberFrom( un );
			nick = m.getNick();
			propic = m.getPropic();
		}
		case UserGuest ug -> nick = ug.getCode();
		default -> throw new RuntimeException();// TODO exception
		}
		return ResponseEntity.ok()
		        .body( new apiWhoamiRes(
		                type,
		                nick,
		                propic ) );
	}

	static public record apiWhoamiRes(
	        String type ,
	        String nick ,
	        String propic )
	{
	}

	// TODO 이거 응답도 바꿔야지.
	@GetMapping( "/me" )
	public ResponseEntity<Member> apiMemberMe (
	        @AuthenticationPrincipal User u ) {

		Member m = getMemberFrom( u );

		return ResponseEntity.ok()
		        .body( m );
	}

	// TODO 이거 응답도 바꿔야지.
	@GetMapping( "/{id}" )
	public ResponseEntity<Member> apiMemberGet (
	        @PathVariable long id ) throws NoSuchEntity {

		// XXX 남의 정보를 어디까지 보여줄?

		Member m = memberSer.get( id );
		return ResponseEntity.ok()
		        .body( m );
	}

	// TODO 이거 응답도 바꿔야지.
	@PostMapping( "/add/basic" )
	public ResponseEntity<MKeyBasic> apiMemberAddBasic (
	        @RequestBody @Validated ReqBody_MemberAddBasic req ) {

		MKeyBasic m;
		try{
			Z_MemberAdd memberReq = req.getMember();
			Z_MKeyBasicAdd keyReq = req.getKey();

			Member member = memberReq.toEntity();
			MKeyBasic mkey = keyReq.toEntity( member, pwEncoder );
			m = memberSer.addMemberWithKeyBasic( member, mkey );
		}
		catch( UniqueViolated e ){
			throw ExceptionalTask.UNPROCESSABLE_ENTITY();
		}
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}

	@AllArgsConstructor
	@Getter
	static public class ReqBody_MemberAddBasic
	{
		@NotNull
		@Valid
		Z_MemberAdd member;

		@NotNull
		@Valid
		Z_MKeyBasicAdd key;

	}

	// TODO 이거 응답도 바꿔야지.
	@PostMapping( "/me/edit" )
	public ResponseEntity<Member> apiMemberEdit (
	        @AuthenticationPrincipal User u ,
	        @RequestBody Z_MemberEdit req ) throws NoSuchEntity {

		Member me = getMemberFrom( u );

		// 프사 필드 업로드 처리
		uploadAndSetFileField( req,
		        ( r )-> r.getPropic(),
		        ( r , v )-> r.setPropic( v ) );

		Member m2 = req.toEntity();

		m2 = memberSer.edit( me, m2 );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m2 );
	}
}
