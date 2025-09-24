package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAttachment;
import xyz.arinmandri.playground.core.board.post.PAuthor;
import xyz.arinmandri.playground.core.board.post.Post;
import xyz.arinmandri.playground.core.board.post.Posts;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.Members;
import xyz.arinmandri.playground.serv.CursorPage;
import xyz.arinmandri.playground.serv.NoSuchEntity;
import xyz.arinmandri.playground.serv.PersistenceServ;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostServ extends PersistenceServ
{
	private final int pageSize = pageSizeDefault;

	final private Members members;
	final private Posts posts;

	@Transactional( readOnly = true )
	public Y_PostDetail get ( long id ) throws NoSuchEntity {
		return posts.get( Y_PostDetail.class, id );// TODO ??? 이거 없으면 어케 되는 거임
	}

	@Transactional( readOnly = true )
	public CursorPage<Y_PostListItem> list () {
		List<Y_PostListItem> rows = posts.getList( Y_PostListItem.class, defaultPageable );
		return new CursorPage<>( rows, pageSize );
	}

	@Transactional( readOnly = true )
	public CursorPage<Y_PostListItem> list ( Long cursor ) {
		List<Y_PostListItem> rows = posts.getList( Y_PostListItem.class, defaultPageable, cursor );
		return new CursorPage<>( rows, pageSize );
	}

	@Transactional( readOnly = true )
	public boolean checkAuthor ( Long postId , Long authorId ) {
		Post p = posts.get( postId );// TODO exception
		return p.getAuthor().getId().equals( authorId );
	}

	/**
	 * 게시글 추가
	 * 
	 * @param addPostReq
	 * @param attachments
	 * @param author
	 * @return 생성된 게시글의 id
	 */
	@Transactional
	public Long add ( Z_PostAdd addPostReq , List<Z_PAttachmentAdd> addAttachmentsReq , Long authorId ) {

		PAuthor author = PAuthor.from(
		        members.findById( Member.class, authorId ) );

		Post p = addPostReq.toEntity( author );

		if( addAttachmentsReq != null ){
			List<PAttachment> atts = new ArrayList<>();

			for( Z_PAttachmentAdd reqAtt : addAttachmentsReq ){
				PAttachment att = reqAtt.toEntity();
				atts.add( att );
			}

			p.setAttachments( atts );
		}

		return posts.add( p );
	}

	@Transactional
	public void edit ( Long originalId , Z_PostEdit req ) {

		Post org = posts.get( originalId );
		Post newOne = Z_PostEdit_toEntity( req, org );
		posts.edit( originalId, newOne );
	}

	private Post Z_PostEdit_toEntity ( Z_PostEdit req , Post org ) {

		return Post.builder()
		        .author( org.getAuthor() )
		        .content( req.content() == null
		                ? org.getContent()
		                : req.content().equals( "" )
		                        ? null
		                        : req.content() )
		        .attachments( req.attachments() == null
		                ? org.getAttachments()
		                : Z_PAttachmentList_toEntity( req.attachments(), org.getAttachments() ) )
		        .build();
	}

	private List<PAttachment> Z_PAttachmentList_toEntity ( List<Z_PAttachment> reqList , List<PAttachment> orgList ) {

		return reqList.stream().map( ( req )-> {
			if( req instanceof Z_PAttachmentNew reqNew ){
				return reqNew.getContent().toEntity();
			}

			if( req instanceof Z_PAttachmentOld reqOld ){
				int originalOrder = reqOld.originalOrder;
				return orgList.get( originalOrder );
			}

			return null;// TODO exception
		} ).toList();
	}

	@Transactional
	public void del ( Long id ) throws NoSuchEntity {
		// XXX
	}

	public static List<Y_PAttachment> collectAttachments ( List<Y_PAttachmentImage> images , List<Y_PAttachmentFile> files ) {
		List<Y_PAttachment> atts = new ArrayList<>();
		if( images != null )
		    atts.addAll( images );
		if( files != null )
		    atts.addAll( files );
		atts.sort( ( a , b )-> Integer.compare( a.getOrder(), b.getOrder() ) );
		return atts;
	}
}
