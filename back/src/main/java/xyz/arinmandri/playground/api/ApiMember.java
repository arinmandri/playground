package xyz.arinmandri.playground.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberSer;
import xyz.arinmandri.playground.mkey.MkeyBasic;
import xyz.arinmandri.playground.mkey.MkeySer;


@RestController
@RequestMapping( "/member" )
@RequiredArgsConstructor
public class ApiMember extends ApiA
{
	final MemberSer memberSer;
	final MkeySer mkeySer;

	@GetMapping( "/{id}" )
	public ResponseEntity<Member> apiMemberGet (
	        @PathVariable long id ) throws NoSuchEntity {

		Member m = memberSer.get( id );
		return ResponseEntity.ok()
		        .body( m );
	}

	@PostMapping( "/add/basic" )
	public ResponseEntity<MkeyBasic> apiMemberAddBasic (
	        @RequestBody MkeySer.AddBasicWithMemberReq req ) {

		MkeyBasic m = mkeySer.addMemberWithKeyBasic( req );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}

	@PostMapping( "/{id}/edit" )
	public ResponseEntity<Member> apiMemberEdit (
	        @PathVariable long id ,
	        @RequestBody MemberSer.EditReq req ) throws NoSuchEntity {

		// TODO auth: author = 로그인회원

		Member m = memberSer.edit( id, req );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( m );
	}
}
