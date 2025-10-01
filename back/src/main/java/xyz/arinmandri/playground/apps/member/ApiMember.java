package xyz.arinmandri.playground.apps.member;

import xyz.arinmandri.playground.apps.a.api.ApiA;
import xyz.arinmandri.playground.apps.a.serv.NoSuchEntity;
import xyz.arinmandri.playground.apps.member.serv.MemberServ;
import xyz.arinmandri.playground.apps.member.serv.Y_MemberForMe;
import xyz.arinmandri.playground.apps.member.serv.Y_MemberForPublic;
import xyz.arinmandri.playground.apps.member.serv.Z_MKeyBasicAdd;
import xyz.arinmandri.playground.apps.member.serv.Z_MemberAdd;
import xyz.arinmandri.playground.apps.member.serv.Z_MemberEdit;
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

	final MemberServ mServ;

	final private PasswordEncoder pwEncoder;

	@GetMapping( "/whoami" )
	public ResponseEntity<apiWhoamiRes> apiWhoami (
	        @AuthenticationPrincipal User user ) throws NoSuchEntity {

		String type = user.getType().toString();
		Long id;
		String nick;
		String propic = null;
		switch( user ){
		case UserNormal un -> {
			id = un.getMemberId();
			Y_MemberForPublic me = mServ.getInfoForPublic( id );
			nick = me.getNick();
			propic = me.getPropic();
		}
		case UserGuest ug -> {
			id = 0L;
			nick = ug.getCode();
		}
		default -> throw new RuntimeException();// TODO exception
		}
		return ResponseEntity.ok()
		        .body( new apiWhoamiRes(
		                type,
		                id,
		                nick,
		                propic ) );
	}

	static public record apiWhoamiRes(
	        String type ,
	        Long id ,// XXX id-handle
	        String nick ,
	        String propic )
	{
	}

	@GetMapping( "/me" )
	public ResponseEntity<Y_MemberForMe> apiMemberMe (
	        @AuthenticationPrincipal User user ) throws NoSuchEntity {

		Long myId = myIdAsMember( user );

		Y_MemberForMe m = mServ.getInfoForMe( myId );

		return ResponseEntity.ok()
		        .body( m );
	}

	@GetMapping( "/{id}" )
	public ResponseEntity<Y_MemberForPublic> apiMemberGet (
	        @PathVariable Long id ) throws NoSuchEntity {

		Y_MemberForPublic m = mServ.getInfoForPublic( id );

		return ResponseEntity.ok()
		        .body( m );
	}

	@PostMapping( "/add/basic" )
	public ResponseEntity<Y_MemberForMe> apiMemberAddBasic (
	        @RequestBody
	        @Validated ReqBody_MemberAddBasic req ) throws NoSuchEntity {

		Z_MemberAdd memberReq = req.getMember();
		Z_MKeyBasicAdd keyReq = req.getKey();

		//// 비밀번호 암호화
		keyReq.setPassword( pwEncoder.encode( keyReq.getPassword() ) );

		mServ.addMemberWithKeyBasic( memberReq, keyReq );

		Y_MemberForMe memberInfo = mServ.getInfoForMe( null );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( memberInfo );
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

	@PostMapping( "/me/edit" )
	public ResponseEntity<Y_MemberForMe> apiMemberEdit (
	        @AuthenticationPrincipal User user ,
	        @RequestBody Z_MemberEdit req ) throws NoSuchEntity {

		Long myId = myIdAsMember( user );

		// 프사 필드 업로드 처리
		uploadAndSetFileField( req,
		        ( r )-> r.getPropic(),
		        ( r , v )-> r.setPropic( v ) );

		mServ.edit( myId, req );

		Y_MemberForMe me = mServ.getInfoForMe( myId );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( me );
	}
}
