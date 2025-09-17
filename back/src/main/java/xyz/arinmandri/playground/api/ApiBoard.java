package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.CursorPage;
import xyz.arinmandri.playground.core.EntityHandler;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.board.PAttFile;
import xyz.arinmandri.playground.core.board.PAttImage;
import xyz.arinmandri.playground.core.board.PAttachment;
import xyz.arinmandri.playground.core.board.Post;
import xyz.arinmandri.playground.core.board.PostRepo;
import xyz.arinmandri.playground.core.board.PostSer;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.LackAuthExcp;

import java.util.ArrayList;
import java.util.List;
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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.RequiredArgsConstructor;
import lombok.With;


@RestController
@RequiredArgsConstructor
public class ApiBoard extends ApiA
{
	final EntityHandler entityHandler;

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

		//// 파일 업로드 처리
		List<EditPostReqAttachment> attachmentsReq = new ArrayList<>();
		for( EditPostReqAttachment attachment : req.attachments ){

			attachment = entityHandler.uploadFileField( attachment,
			        ( r )-> r.url(),
			        ( r , v )-> r.withUrl( v ) );
			attachmentsReq.add( attachment );
		}

		// TODO 파일 타입인 경우 size

		List<PAttachment> attachments2 = attachmentsReq.stream()
		        .map( a-> a.toEntity() )
		        .toList();

		Post p = req.toEntity( m );
		p = postSer.add( p, attachments2 );
		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( p );
	}

	static public record AddPostReq(
	        String content ,
	        List<EditPostReqAttachment> attachments )
	{

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
	        @RequestBody EditPostReq req ) throws LackAuthExcp {

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

	static public record EditPostReq(
	        String content ,
	        List<EditPostReqAttachment> attachments )
	{

		Post toEntity () {
			return Post.builder()
			        .content( content )
			        .build();
		}
	}

	@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
	@JsonSubTypes( {
	        @JsonSubTypes.Type( value = EditPostReqAttachmentImage.class , name = EditPostReqAttachmentImage.type ),
	        @JsonSubTypes.Type( value = EditPostReqAttachmentFile.class , name = EditPostReqAttachmentFile.type )
	} )
	static public interface EditPostReqAttachment
	{
		public String url ();

		public EditPostReqAttachment withUrl ( String url );

		public PAttachment toEntity ();
	}

	static public record EditPostReqAttachmentImage(
	        @With String url ) implements EditPostReqAttachment
	{
		public static final String type = "image";// TODO 도메인에서가져와?

		@Override
		public PAttImage toEntity () {
			return PAttImage.builder()
			        .url( url )
			        .build();
		}
	}

	static public record EditPostReqAttachmentFile(
	        @With String url ) implements EditPostReqAttachment
	{
		public static final String type = "file";// TODO 도메인에서가져와?

		@Override
		public PAttFile toEntity () {
			return PAttFile.builder()
			        .url( url )
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
