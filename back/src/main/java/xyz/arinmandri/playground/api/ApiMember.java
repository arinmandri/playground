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
import xyz.arinmandri.playground.service.NoSuchEntity;
import xyz.arinmandri.playground.service.member.Member;
import xyz.arinmandri.playground.service.member.MemberSer;


@RestController
@RequestMapping( "/member" )
@RequiredArgsConstructor
public class ApiMember extends ApiA
{
	final MemberSer memberSer;

	@GetMapping( "/{id}" )
	public ResponseEntity<Member> apiMemberGet (
	        @PathVariable long id ) throws NoSuchEntity {
		Member m = memberSer.get( id );
		return ResponseEntity.ok()
		        .body( m );
	}

	@PostMapping( "/add" )
	public ResponseEntity<Member> apiMemberAdd (
	        @RequestBody MemberSer.AddReq req ) {
		Member m = memberSer.add( req );
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
