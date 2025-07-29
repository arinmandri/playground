package xyz.arinmandri.playground.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.board.Post;
import xyz.arinmandri.playground.core.board.PostSer;
import xyz.arinmandri.playground.core.mkey.MkeyBasic;


@RestController
@RequiredArgsConstructor
public class ApiBoard extends ApiA
{

	final PostSer postSer;

	// TEST
	@GetMapping( "/board" )
	public ResponseEntity<List<Post>> apiBoard () {
		List<Post> list = postSer.all();
		return ResponseEntity.ok()
		        .body( list );
	}

	@GetMapping( "/post/{id}" )
	public ResponseEntity<Post> apiPostGet (
	        @PathVariable long id ) {

		Post p;
		try{
			p = postSer.get( id );
		}
		catch( NoSuchEntity e ){
			throw new ExceptionalTask( ExcpType.NoSuchEntity, e );
		}
		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/add" )
	public ResponseEntity<Post> apiPostAdd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody PostSer.AddReq req ) {

		MkeyBasic mk = getMkeyBasicFrom( userDetails );

		Post p;
		try{
			p = postSer.add( mk.getOwner().getId(), req );
		}
		catch( NoSuchEntity e ){
			throw new ExceptionalTask( ExcpType.NoSuchEntity, e );
		}
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( p );
	}

	@PostMapping( "/post/{id}/edit" )
	public ResponseEntity<Post> apiPostEdit (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ,
	        @RequestBody PostSer.EditReq req ) {

		// TODO auth: author = 로그인회원

		Post p;
		try{
			p = postSer.edit( id, req );
		}
		catch( NoSuchEntity e ){
			throw new ExceptionalTask( ExcpType.NoSuchEntity, e );
		}
		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/{id}/del" )
	public ResponseEntity<Void> apiPostDel (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ) {

		// TODO auth: author = 로그인회원

		postSer.del( id );
		return ResponseEntity.ok().build();
	}
}
