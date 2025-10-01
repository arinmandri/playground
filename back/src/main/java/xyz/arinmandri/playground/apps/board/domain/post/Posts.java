package xyz.arinmandri.playground.apps.board.domain.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class Posts
{
	private final PostRepo pRepo;

	public Post get ( long id ) {
		return pRepo.findById( Post.class, id );
	}

	public < T > T get ( Class<T> type , long id ) {
		return pRepo.findById( type, id );
	}

	public < T > List<T> getList ( Class<T> type , Pageable pagable ) {
		return pRepo.findAllByOrderByIdDesc( type, pagable );
	}

	public < T > List<T> getList ( Class<T> type , Pageable pagable , Long cursor ) {
		return pRepo.findByIdLessThanOrderByIdDesc( type, cursor, pagable );
	}

	@Transactional
	public Long add ( Post newPost ) {
		processPAttachments( newPost );

		Post p = pRepo.save( newPost );

		return p.getId();
	}

	@Transactional
	public void edit ( Long id , Post data ) {
		processPAttachments( data );

		data.setId( id );
		pRepo.save( data );
	}

	@Transactional
	public Post del ( Long id ) {
		Post p = pRepo.findById( Post.class, id );
		pRepo.delete( p );
		return p;
	}

	/**
	 * attachments 필드를 기준으로 파생 값 설정
	 * - 항목 순서대로 order=1,2,3,...
	 * - 항목의 belongs_to = p
	 * - 첨부이미지 목록, 첨부파일 목록 등 종류별 목록 set
	 * 
	 * @param p
	 */
	private void processPAttachments ( Post p ) {

		List<PAttachment> atts = p.getAttachments();

		int order = 0;
		List<PAttachmentImage> listImage = new ArrayList<>();
		List<PAttachmentFile> listFile = new ArrayList<>();
		for( PAttachment att : atts ){
			att.setOrder( order++ );
			att.setBelongsTo( p );

			if( att instanceof PAttachmentImage itemImage )
			    listImage.add( itemImage );
			if( att instanceof PAttachmentFile itemFile )
			    listFile.add( itemFile );
		}
		p.attachmentsImage = listImage;
		p.attachmentsFile = listFile;
	}
}
