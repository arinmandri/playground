package xyz.arinmandri.playground.core.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberSer;


@Service
@RequiredArgsConstructor
public class PostSer
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

	public void del ( long id ) {
		repo.deleteById( id );
	}

	@Transactional
	public Post add ( long author__m , AddReq req ) throws NoSuchEntity {
		Member author = memberSer.get( author__m );
		return repo.save( req.toEntity( author ) );
	}

	@AllArgsConstructor
	@Getter
	static public class AddReq
	{
		private String content;

		Post toEntity ( Member author ) {
			return Post.builder()
			        .author( author )
			        .content( content )
			        .build();
		}
	}

	@Transactional
	public Post edit ( Long id , EditReq req ) throws NoSuchEntity {
		Post p = repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Post.class , id ) );
		p.update( req.toEntity() );
		return p;
	}

	@AllArgsConstructor
	@Getter
	static public class EditReq
	{
		private String content;

		Post toEntity () {
			return Post.builder()
			        .content( content )
			        .build();
		}
	}
}
