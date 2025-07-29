package xyz.arinmandri.playground.core.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;
import xyz.arinmandri.playground.core.member.MemberSer;


@Service
@RequiredArgsConstructor
public class PostSer extends PersistenceSer
{
	final private PostRepo repo;

	final private MemberSer memberSer;

	public List<Post> all () {
		return repo.findAll();
	}

	public Post get ( long id ) throws NoSuchEntity {
		return repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Post.class , id ) );
	}

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
}
