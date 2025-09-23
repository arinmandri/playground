package xyz.arinmandri.playground.core.member;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class Members
{

	final private MemberRepo mRepo;
	final private MKeyBasicRepo kbRepo;

	public < T > T findById ( Class<T> type , Long id ) {
		return mRepo.findById( type, id );
	}

	public Long add ( Member newMember , MKeyBasic key ) {
		// TODO duple
		Member m = mRepo.save( newMember );
		kbRepo.save( key );
		return m.getId();
	}

	public void edit ( Long id , Member data ) {
		data.setId( id );
		mRepo.save( data );
	}
}
