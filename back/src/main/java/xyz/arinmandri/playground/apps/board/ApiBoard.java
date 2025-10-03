package xyz.arinmandri.playground.apps.board;

import xyz.arinmandri.playground.apps.board.serv.PostServ;
import xyz.arinmandri.playground.apps.board.serv.Y_PostDetail;
import xyz.arinmandri.playground.apps.board.serv.Y_PostListItem;
import xyz.arinmandri.playground.apps.board.serv.Z_PAttachmentAdd;
import xyz.arinmandri.playground.apps.board.serv.Z_PAttachmentAddFile;
import xyz.arinmandri.playground.apps.board.serv.Z_PAttachmentAddImage;
import xyz.arinmandri.playground.apps.board.serv.Z_PAttachmentNew;
import xyz.arinmandri.playground.apps.board.serv.Z_PostAdd;
import xyz.arinmandri.playground.apps.board.serv.Z_PostEdit;
import xyz.arinmandri.playground.apps.a.api.ApiA;
import xyz.arinmandri.playground.apps.a.serv.CursorPage;
import xyz.arinmandri.playground.apps.a.serv.exception.NoSuchEntity;
import xyz.arinmandri.playground.security.LackAuthExcp;
import xyz.arinmandri.playground.security.user.User;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
	final PostServ pServ;

	@GetMapping( "/post/{id}" )
	public ResponseEntity<Y_PostDetail> apiPostGet (
	        @PathVariable long id
	) {

		Y_PostDetail p;
		try{
			p = pServ.getPostDetail( id );
		}
		catch( NoSuchEntity e ){
			throw ExceptionalTask.NOT_FOUND();
		}
		return ResponseEntity.ok()
		        .body( p );
	}

	@GetMapping( "/post/list" )
	public ResponseEntity<CursorPage<Y_PostListItem>> apiPostList (
	        @RequestParam( required = false ) Long cursor
	) {

		CursorPage<Y_PostListItem> p;
		p = cursor == null
		        ? pServ.list()
		        : pServ.list( cursor );

		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/add" )
	public ResponseEntity<Y_PostDetail> apiPostAdd (
	        @AuthenticationPrincipal User user ,
	        @RequestBody
	        @Valid Z_PostAdd req
	) {

		Long myId = myIdAsMember( user );

		List<Z_PAttachmentAdd> addAttachmentsReq = null;

		//// 파일 업로드 처리
		if( req.attachments() != null ){
			addAttachmentsReq = req.attachments().stream().map( reqNewAtt-> reqNewAtt.getContent() ).toList();
			for( Z_PAttachmentAdd reqAtt : addAttachmentsReq ){
				if( reqAtt instanceof Z_PAttachmentAddImage attImage ){
					uploadAndSetFileField( attImage,
					        ( r )-> r.getUrl(),
					        ( r , url )-> {
						        r.setUrl( url );
					        } );
				}
				if( reqAtt instanceof Z_PAttachmentAddFile attFile ){
					uploadFileField( attFile,
					        ( r )-> r.getUrl(),
					        ( r , ltf )-> {
						        String uploadedUrl = s3Serv.s3Upload( ltf.path() ).toString();
						        r.setUrl( uploadedUrl );
						        attFile.setSize( ltf.size() );
						        return null;
					        } );
				}
			}
		}

		Long id = pServ.add( req, addAttachmentsReq, myId );

		Y_PostDetail p;
		try{
			p = pServ.getPostDetail( id );
		}
		catch( NoSuchEntity e ){
			throw ExceptionalTask.NOT_FOUND();// TODO 이 경우가 나와???
		}

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( p );
	}

	@PostMapping( "/post/{post-id}/edit" )
	public ResponseEntity<Y_PostDetail> apiPostEdit (
	        @AuthenticationPrincipal User user ,
	        @PathVariable( name = "post-id" ) long postId ,
	        @RequestBody Z_PostEdit req
	) throws LackAuthExcp , NoSuchEntity {

		Long myId = myIdAsMember( user );

		if( !pServ.checkAuthor( postId, myId ) ){
			throw new ExceptionalTask( HttpStatus.FORBIDDEN, "내 것이 아니면 못 건듧니다." );
		}

		//// 파일 업로드 처리 XXX addPost랑 중복
		if( req.attachments() != null ){
			final List<Z_PAttachmentAdd> addAttachmentsReq = new ArrayList<>();

			req.attachments().stream().forEach( nooReq-> {
				if( nooReq instanceof Z_PAttachmentNew newReq )
				    addAttachmentsReq.add( newReq.getContent() );
			} );

			for( Z_PAttachmentAdd reqAtt : addAttachmentsReq ){
				if( reqAtt instanceof Z_PAttachmentAddImage attImage ){
					uploadAndSetFileField( attImage,
					        ( r )-> r.getUrl(),
					        ( r , url )-> {
						        r.setUrl( url );
					        } );
				}
				if( reqAtt instanceof Z_PAttachmentAddFile attFile ){
					uploadFileField( attFile,
					        ( r )-> r.getUrl(),
					        ( r , ltf )-> {
						        String uploadedUrl = s3Serv.s3Upload( ltf.path() ).toString();
						        r.setUrl( uploadedUrl );
						        attFile.setSize( ltf.size() );
						        return null;
					        } );
				}
			}
		}

		pServ.edit( postId, req );

		Y_PostDetail p = pServ.getPostDetail( postId );

		return ResponseEntity.ok()
		        .body( p );
	}

	@PostMapping( "/post/{post-id}/del" )
	public ResponseEntity<Void> apiPostDel (
	        @AuthenticationPrincipal User user ,
	        @PathVariable( name = "post-id" ) long postId
	) throws LackAuthExcp , NoSuchEntity {

		Long myId = myIdAsMember( user );

		if( !pServ.checkAuthor( postId, myId ) ){
			throw new ExceptionalTask( HttpStatus.FORBIDDEN, "내 것이 아니면 못 건듧니다." );
		}

		pServ.del( postId );

		return ResponseEntity.ok().build();
	}
}
