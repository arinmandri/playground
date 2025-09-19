package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.CursorPage;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;
import xyz.arinmandri.playground.core.member.Member;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PostSer extends PersistenceSer
{
	private final int pageSize = pageSizeDefault;

	final private PostRepo repo;
	final private PAttachmentRepo attRepo;

	@Transactional( readOnly = true )
	public Y_PostDetail get ( long id ) throws NoSuchEntity {
		return repo.findById( id, Y_PostDetail.class );// TODO ??? 이거 없으면 어케 되는 거임
	}

	@Transactional( readOnly = true )
	public CursorPage<Y_PostListItem> list () {
		List<Y_PostListItem> rows = repo.findAllByOrderByIdDesc( defaultPageable );
		return new CursorPage<>( rows, pageSize );
	}

	@Transactional( readOnly = true )
	public CursorPage<Y_PostListItem> list ( Long cursor ) {
		List<Y_PostListItem> rows = repo.findByIdLessThanOrderByIdDesc( cursor, defaultPageable );
		return new CursorPage<>( rows, pageSize );
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
	public Long add ( Z_PostAdd addPostReq , List<PAttachment> attachments , Member author ) {

		Post p = repo.save( addPostReq.toEntity( author ) );

		if( attachments != null ){
			int order = 1;
			for( PAttachment attachment : attachments ){
				attachment.setOrder( order++ );
				attachment.setBelongsTo( p );
				attRepo.save( attachment );
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
			atts.addAll( list );
		}
		atts.sort( ( a , b )-> Integer.compare( a.getOrder(), b.getOrder() ) );
		return atts;
	}
}
