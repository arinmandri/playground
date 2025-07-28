package xyz.arinmandri.playground.core.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;


@Service
@RequiredArgsConstructor
public class MemberSer
{
	final private MemberRepo repo;

	public Member get ( long id ) throws NoSuchEntity {
		return repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Member.class , id ) );
	}

	@Transactional
	public Member edit ( Long id , EditReq req ) throws NoSuchEntity {
		Member m = repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Member.class , id ) );
		m.update( req.toEntity() );
		return m;
	}

	@AllArgsConstructor
	@Getter
	static public class AddReq
	{
		String nick;
		String email;

		public Member toEntity () {
			return Member.builder()
			        .nick( nick )
			        .email( email )
			        .build();
		}
	}

	@AllArgsConstructor
	@Getter
	static public class EditReq
	{
		String nick;
		String email;

		Member toEntity () {
			return Member.builder()
			        .nick( nick )
			        .email( email )
			        .build();
		}
	}
}
