package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.EntityHandler;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer.UniqueViolated;
import xyz.arinmandri.playground.core.member.MKeyBasic;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberSer;
import xyz.arinmandri.playground.security.user.User;
import xyz.arinmandri.playground.security.user.UserGuest;
import xyz.arinmandri.playground.security.user.UserNormal;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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

import lombok.RequiredArgsConstructor;
import lombok.With;


@RestController
@RequestMapping( "/member" )
@RequiredArgsConstructor
public class ApiMember extends ApiA
{

	final MemberSer memberSer;
	final EntityHandler entityHandler;

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
	        @AuthenticationPrincipal User u ,
	        @RequestBody @Validated apiMemberAddBasicReq req ) {

		MKeyBasic m;
		try{
			apiMemberAddBasicReq.apiMemberAddBasicReqMember memberReq = req.member;
			apiMemberAddBasicReq.apiMemberAddBasicReqKey keyReq = req.key;

			// 프사 필드 업로드 처리
			memberReq = entityHandler.uploadFileField( memberReq,
			        ( r )-> r.propic(),
			        ( r , v )-> r.withPropic( v ) );

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

	static public record apiMemberAddBasicReq(
	        @NotNull @Valid apiMemberAddBasicReqMember member ,
	        @NotNull @Valid apiMemberAddBasicReqKey key )
	{

		static public record apiMemberAddBasicReqMember(
		        @NotNull @NotBlank String nick ,
		        String email ,// TODO email 형식
		        @With String propic )
		{
			public Member toEntity () {

				return Member.builder()
				        .nick( nick.equals( "" ) ? null : nick )
				        .email( email == null || email.equals( "" ) ? null : email )
				        .propic( propic == null || propic.equals( "" ) ? null : propic )
				        .build();
			}
		}

		static public record apiMemberAddBasicReqKey(
		        // XXX 제한 추가. 길이라든가 정규식 뭐 있겠지.
		        @NotNull String keyname ,
		        @NotNull String password )
		{
			public MKeyBasic toEntity ( Member owner , PasswordEncoder pwEncoder ) {
				return MKeyBasic.builder()
				        .owner( owner )
				        .keyname( keyname )
				        .password( pwEncoder.encode( password ) )
				        .build();
			}
		}
	}

	// TODO 이거 응답도 바꿔야지.
	@PostMapping( "/me/edit" )
	public ResponseEntity<Member> apiMemberEdit (
	        @AuthenticationPrincipal User u ,
	        @RequestBody EditMemberReq req ) throws NoSuchEntity {

		Member me = getMemberFrom( u );

		// 프사 필드 업로드 처리
		req = entityHandler.uploadFileField( req,
		        ( r )-> r.propic(),
		        ( r , v )-> r.withPropic( v ) );

		Member m2 = req.toEntity();

		m2 = memberSer.edit( me, m2 );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m2 );
	}

	static public record EditMemberReq(
	        String nick ,
	        String email ,
	        @With String propic )
	{
		Member toEntity () {
			return Member.builder()
			        .nick( nick )
			        .email( email )
			        .propic( propic )
			        .build();
		}
	}
}
