package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAttachment;
import xyz.arinmandri.playground.core.board.post.PAttachmentRepo;
import xyz.arinmandri.playground.core.board.post.PAuthor;
import xyz.arinmandri.playground.core.board.post.PAuthorRepo;
import xyz.arinmandri.playground.core.board.post.Post;
import xyz.arinmandri.playground.core.board.post.PostRepo;
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

	final private PostRepo repo;
	final private PAttachmentRepo attRepo;
	final private PAuthorRepo athrRepo;

	@Transactional( readOnly = true )
	public Y_PostDetail get ( long id ) throws NoSuchEntity {
		return repo.findById( Y_PostDetail.class, id );// TODO ??? 이거 없으면 어케 되는 거임
	}

	@Transactional( readOnly = true )
	public CursorPage<Y_PostListItem> list () {
		List<Y_PostListItem> rows = repo.findAllByOrderByIdDesc( Y_PostListItem.class, defaultPageable );
		return new CursorPage<>( rows, pageSize );
	}

	@Transactional( readOnly = true )
	public CursorPage<Y_PostListItem> list ( Long cursor ) {
		List<Y_PostListItem> rows = repo.findByIdLessThanOrderByIdDesc( Y_PostListItem.class, cursor, defaultPageable );
		return new CursorPage<>( rows, pageSize );
	}

	@Transactional( readOnly = true )
	public boolean checkAuthor ( Long postId , Long authorId ) {
		Post p = repo.findById( postId ).orElseThrow( null );// TODO exception
		PAuthor m = athrRepo.findById( authorId ).orElseThrow( null );// TODO exception
		return p.getAuthor().equals( m );
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

		PAuthor author = athrRepo.findById( authorId )
		        .orElseThrow( null );// TODO exception

		Post p = addPostReq.toEntity( author );
		p = repo.save( p );

		if( addAttachmentsReq != null ){
			List<PAttachment> atts = new ArrayList<>();

			for( Z_PAttachmentAdd reqAtt : addAttachmentsReq ){
				PAttachment att = reqAtt.toEntity();
				atts.add( att );
			}
			
			p.setAttachments( atts );
			for( PAttachment item : atts ){
				attRepo.save( item );
			}
		}

		return p.getId();
	}

	@Transactional
	public void edit ( Long originalId , Z_PostEdit newData ) {
		Post postOriginal = repo.findById( originalId ).get();
		Post newOne = newData.toEntity();
		postOriginal.update( newOne );
	}

	@Transactional
	public void del ( Long id ) throws NoSuchEntity {
		Post p = repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Post.class, id ) );
		repo.delete( p );
	}

	public static List<Y_PAttachment> collectAttachments ( List<? extends Y_PAttachment>... attachmentsSomeTypeLists ) {
		List<Y_PAttachment> atts = new ArrayList<>();
		for( List<? extends Y_PAttachment> list : attachmentsSomeTypeLists ){
			if( list != null )
			    atts.addAll( list );
		}
		atts.sort( ( a , b )-> Integer.compare( a.getOrder(), b.getOrder() ) );
		return atts;
	}
}
