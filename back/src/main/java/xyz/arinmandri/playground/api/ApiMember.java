package xyz.arinmandri.playground.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
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
	        @RequestBody MemberSer.AddBasicWithMemberReq req ){

		MKeyBasic m;
		try{
			m = memberSer.addMemberWithKeyBasic( req );
		}
		catch( UniqueViolated e ){
			throw new ExceptionalTask( ExcpType.EntityDuplicate, e );
		}
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}

	// TODO 이거 응답도 바꿔야지.
	@PostMapping( "/{id}/edit" )
	public ResponseEntity<Member> apiMemberEdit (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ,
	        @RequestBody MemberSer.EditReq req ) throws NoSuchEntity {

		// TODO auth: author = 로그인회원

		Member m = memberSer.edit( id, req );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}
}
