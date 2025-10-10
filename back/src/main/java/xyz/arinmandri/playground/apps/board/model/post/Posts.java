package xyz.arinmandri.playground.apps.board.model.post;

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

		Post p = pRepo.save( newPost );

		return p.getId();
	}

	@Transactional
	public Post edit ( Long id , Post data ) {

		data.setId( id );
		pRepo.save( data );

		return data;
	}

	@Transactional
	public Post del ( Long id ) {

		Post p = pRepo.findById( Post.class, id );
		if( p == null ) return null;

		pRepo.delete( p );
		return p;
	}
}
