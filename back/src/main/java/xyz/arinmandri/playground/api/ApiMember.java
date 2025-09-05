package xyz.arinmandri.playground.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.With;
import xyz.arinmandri.playground.core.EntityHandler;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer.UniqueViolated;
import xyz.arinmandri.playground.core.member.MKeyBasic;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberSer;


@RestController
@RequestMapping( "/member" )
@RequiredArgsConstructor
public class ApiMember extends ApiA
{

	final MemberSer memberSer;
	final EntityHandler entityHandler;

	final private PasswordEncoder pwEncoder;

	// @GetMapping( "/me" )
	// public ResponseEntity<Member> apiMemberMe (
	//         @AuthenticationPrincipal UserDetails userDetails ) throws NoSuchEntity {

	// 	// XXX 남의 정보를 어디까지 보여줄?

	// 	Member m = memberSer.get( id );
	// 	return ResponseEntity.ok()
	// 	        .body( m );
	// }

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
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody @Validated apiMemberAddBasicReqBody req ) {

		MKeyBasic m;
		try{
			AddMemberReq memberReq = req.member;
			AddMKeyBasicReq keyReq = req.key;

			// 프사 필드 업로드 처리
			memberReq = entityHandler.uploadFileField( memberReq,
			        ( r )-> r.propic(),
			        ( r , v )-> r.withPropic( v ) );

			Member member = memberReq.toEntity();
			MKeyBasic mkey = keyReq.toEntity( member, pwEncoder );
			m = memberSer.addMemberWithKeyBasic( member, mkey );
		}
		catch( UniqueViolated e ){
			throw new ExceptionalTask( ExcpType.EntityDuplicate, e );
		}
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}

	static public record apiMemberAddBasicReqBody(
	        @NotNull @Valid AddMemberReq member ,
	        @NotNull @Valid AddMKeyBasicReq key )
	{
	}

	static public record AddMemberReq(
	        @NotNull @NotBlank String nick ,
	        @NotNull @NotBlank String email ,
	        @With String propic )
	{
		public Member toEntity () {

			return Member.builder()
			        .nick( nick.equals( "" ) ? null : nick )
			        .email( email.equals( "" ) ? null : email )
			        .propic( propic == null || propic.equals( "" ) ? null : propic )
			        .build();
		}
	}

	static public record AddMKeyBasicReq(
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

	// TODO 이거 응답도 바꿔야지.
	@PostMapping( "/{id}/edit" )
	public ResponseEntity<Member> apiMemberEdit (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ,
	        @RequestBody EditMemberReq req ) throws NoSuchEntity {

		// TODO auth: author = 로그인회원

		Member m = req.toEntity();
		m = memberSer.edit( id, m );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}

	static public record EditMemberReq(
	        String nick ,
	        String email ,
	        String propic )
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
