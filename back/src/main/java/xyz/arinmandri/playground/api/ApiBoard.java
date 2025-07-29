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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.board.Post;
import xyz.arinmandri.playground.core.board.PostRepo;
import xyz.arinmandri.playground.core.board.PostSer;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.LackAuthExcp;


@RestController
@RequiredArgsConstructor
public class ApiBoard extends ApiA
{

	final PostSer postSer;

	final private PostRepo postRepo;

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
		        .orElseThrow( ()-> new ExceptionalTask( ExcpType.NoSuchEntity, new NoSuchEntity( Post.class, id ) ) );

		if( !p.getAuthor().equals( m ) ){
			throw new LackAuthExcp( "내 것이 아니면 못 건듧니다." );
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
		        .orElseThrow( ()-> new ExceptionalTask( ExcpType.NoSuchEntity, new NoSuchEntity( Post.class, id ) ) );

		if( !p.getAuthor().equals( m ) ){
			throw new LackAuthExcp( "내 것이 아니면 못 건듧니다." );
		}

		postSer.del( p );
		return ResponseEntity.ok().build();
	}
}
