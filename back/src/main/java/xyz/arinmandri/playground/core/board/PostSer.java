package xyz.arinmandri.playground.core.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.CursorPage;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;


@Service
@RequiredArgsConstructor
public class PostSer extends PersistenceSer
{
	private final int pageSize = pageSizeDefault;

	final private PostRepo repo;

	@Transactional( readOnly = true )
	public Post get ( long id ) throws NoSuchEntity {
		return repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Post.class , id ) );
	}

	@Transactional
	public void del ( Post post ) {
		repo.delete( post );
	}

	@Transactional
	public Post add ( Post post ) {
		return repo.save( post );
	}

	@Transactional
	public Post edit ( Post postOriginal , Post postNew ) {
		postOriginal.update( postNew );
		return postOriginal;
	}

	@Transactional( readOnly = true )
	public CursorPage<Post> list () {
		List<Post> rows = repo.findAllByOrderByIdDesc( defaultPageable );
		return new CursorPage<>( rows, pageSize );
	}

	@Transactional( readOnly = true )
	public CursorPage<Post> list ( Long cursor ) {
		List<Post> rows = repo.findByIdLessThanOrderByIdDesc( cursor, defaultPageable );
		return new CursorPage<>( rows, pageSize );
	}
}
