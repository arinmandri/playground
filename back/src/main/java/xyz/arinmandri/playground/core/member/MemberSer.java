package xyz.arinmandri.playground.core.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;


@Service
@RequiredArgsConstructor
public class MemberSer extends PersistenceSer
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
		String propic;

		public Member toEntity () {
			if( nick.equals( "" ) ) nick = null;
			if( email.equals( "" ) ) email = null;
			if( propic.equals( "" ) ) propic = null;

			return Member.builder()
			        .nick( nick )
			        .email( email )
			        .propic( propic )
			        .build();
		}
	}

	@AllArgsConstructor
	@Getter
	static public class EditReq
	{
		String nick;
		String email;
		String propic;

		Member toEntity (){

			return Member.builder()
			        .nick( nick )
			        .email( email )
			        .propic( propic )
			        .build();
		}
	}
}
