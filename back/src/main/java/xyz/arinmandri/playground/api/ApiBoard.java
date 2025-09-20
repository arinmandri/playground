package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.CursorPage;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.board.PostSer;
import xyz.arinmandri.playground.core.board.Y_PostDetail;
import xyz.arinmandri.playground.core.board.Y_PostListItem;
import xyz.arinmandri.playground.core.board.Z_PAttachmentAdd;
import xyz.arinmandri.playground.core.board.Z_PAttachmentFileAdd;
import xyz.arinmandri.playground.core.board.Z_PostAdd;
import xyz.arinmandri.playground.core.board.Z_PostEdit;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.LackAuthExcp;

import jakarta.validation.Valid;

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

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ApiBoard extends ApiA
{
	final PostSer postSer;

	@GetMapping( "/post/{id}" )
	public ResponseEntity<Y_PostDetail> apiPostGet (
	        @PathVariable long id ) {

		Y_PostDetail p;
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
	public ResponseEntity<CursorPage<Y_PostListItem>> apiPostList (
	        @RequestParam( required = false ) Long cursor ) {

		CursorPage<Y_PostListItem> p;
		p = cursor == null
		        ? postSer.list()
		        : postSer.list( cursor );

		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/add" )
	public ResponseEntity<Y_PostDetail> apiPostAdd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody
	        @Valid Z_PostAdd req
	) {

		Member me = getMemberFrom( userDetails );

		//// 파일 업로드 처리
		if( req.attachments() != null ){
			for( Z_PAttachmentAdd reqAtt : req.attachments() ){

				uploadFileField( reqAtt,
				        ( r )-> r.getUrl(),
				        ( r , ltf )-> {
					        String uploadedUrl = s3Ser.s3Upload( ltf.path() ).toString();
					        r.setUrl( uploadedUrl );
					        if( reqAtt instanceof Z_PAttachmentFileAdd attFile ){
						        attFile.setSize( ltf.size() );
					        }
					        return null;
				        } );
			}
		}

		Long id = postSer.add( req, req.attachments(), me );

		Y_PostDetail p;
		try{
			p = postSer.get( id );
		}
		catch( NoSuchEntity e ){
			throw ExceptionalTask.NOT_FOUND();// TODO 이 경우가 나와???
		}

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( p );
	}

	@PostMapping( "/post/{id}/edit" )
	public ResponseEntity<Y_PostDetail> apiPostEdit (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id ,
	        @RequestBody Z_PostEdit req
	) throws LackAuthExcp , NoSuchEntity {

		Member m = getMemberFrom( userDetails );

		Y_PostDetail p = postSer.get( id );

		if( !p.getAuthor().equals( m ) ){
			throw new ExceptionalTask( HttpStatus.FORBIDDEN, "내 것이 아니면 못 건듧니다." );
		}

		postSer.edit( id, req );

		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/{id}/del" )
	public ResponseEntity<Void> apiPostDel (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @PathVariable long id
	) throws LackAuthExcp , NoSuchEntity {

		Member m = getMemberFrom( userDetails );

		Y_PostDetail p = postSer.get( id );

		if( !p.getAuthor().equals( m ) ){
			throw new ExceptionalTask( HttpStatus.FORBIDDEN, "내 것이 아니면 못 건듧니다." );
		}

		postSer.del( id );
		return ResponseEntity.ok().build();
	}
}
