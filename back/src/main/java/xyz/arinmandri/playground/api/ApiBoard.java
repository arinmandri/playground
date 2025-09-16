package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.CursorPage;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.board.Post;
import xyz.arinmandri.playground.core.board.PostRepo;
import xyz.arinmandri.playground.core.board.PostSer;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.LackAuthExcp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ApiBoard extends ApiA
{

	final PostSer postSer;

	final private PostRepo postRepo;

	@GetMapping( "/post/{id}" )
	public ResponseEntity<Post> apiPostGet (
	        @PathVariable long id ) {

		Post p;
		try{
			p = postSer.get( id );
		}
		catch( NoSuchEntity e ){
			throw ExceptionalTask.NOT_FOUND();
		}
		return ResponseEntity.ok()
		        .body( p );
	}

	@GetMapping( "/post/list" )
	public ResponseEntity<CursorPage<Post>> apiPostList (
	        @RequestParam( required = false ) Long cursor ) {

		CursorPage<Post> p;
		p = cursor == null
		        ? postSer.list()
		        : postSer.list( cursor );

		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/add" )
	public ResponseEntity<Post> apiPostAdd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody AddPostReq req ) {

		Member m = getMemberFrom( userDetails );

		Post p = req.toEntity( m );
		p = postSer.add( p );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( p );
	}

	@AllArgsConstructor
	@Getter
	static public class AddPostReq
	{
		private String content;

		Post toEntity ( Member author ) {
			return Post.builder()
			        .author( author )
			        .content( content )
			        .build();
		}
	}

	@PostMapping( "/post/{id}/edit" )
	public ResponseEntity<Post> apiPostEdit (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ,
	        @RequestBody EditReq req ) throws LackAuthExcp {

		Member m = getMemberFrom( userDetails );

		Post p = postRepo.findById( id )
		        .orElseThrow( ()-> ExceptionalTask.NOT_FOUND() );

		if( !p.getAuthor().equals( m ) ){
			throw new ExceptionalTask( HttpStatus.FORBIDDEN, "내 것이 아니면 못 건듧니다." );
		}

		p = postSer.edit( p, req.toEntity() );
		return ResponseEntity.ok()
		        .body( p );
	}

	@AllArgsConstructor
	@Getter
	static public class EditReq
	{
		private String content;

		Post toEntity () {
			return Post.builder()
			        .content( content )
			        .build();
		}
	}

	@PostMapping( "/post/{id}/del" )
	public ResponseEntity<Void> apiPostDel (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ) throws LackAuthExcp {

		Member m = getMemberFrom( userDetails );

		Post p = postRepo.findById( id )
		        .orElseThrow( ()-> ExceptionalTask.NOT_FOUND() );

		if( !p.getAuthor().equals( m ) ){
			throw new ExceptionalTask( HttpStatus.FORBIDDEN, "내 것이 아니면 못 건듧니다." );
		}

		postSer.del( p );
		return ResponseEntity.ok().build();
	}
}
