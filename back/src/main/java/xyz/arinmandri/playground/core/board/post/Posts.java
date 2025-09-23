package xyz.arinmandri.playground.core.board.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class Posts
{
	private final PostRepo pRepo;
	final private PAuthorRepo athrRepo;
	private final PAttachmentRepo attRepo;

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

	public Long add ( Post newPost ) {

		Post p = pRepo.save( newPost );
		athrRepo.save( p.getAuthor() );

		for( PAttachment item : newPost.getAttachments() ){
			attRepo.save( item );
		}

		return p.getId();
	}

	public void edit ( Long id , Post data ) {
		data.setId( id );
		pRepo.save( data );
	}
}
