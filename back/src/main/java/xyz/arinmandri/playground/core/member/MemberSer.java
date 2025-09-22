package xyz.arinmandri.playground.core.member;

import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


/**
 * TODO 응용 서비스 나누기
 * Member, MKey
 * 
 * Member 생성시 반드시 Key 하나와 함께 생성한다.
 */
@Service
@RequiredArgsConstructor
public class MemberSer extends PersistenceSer
{
	final private MemberRepo repo;
	final private MKeyBasicRepo mkeyBasicRepo;

	public MKeyBasic getMKeyBasic ( String keyname ) {
		MKeyBasic k = mkeyBasicRepo.findByKeyname( keyname ).orElse( null );
		return k;
	}

	public Y_MemberForMe getInfoForMe ( Long id ) throws NoSuchEntity {
		// TODO NoSuchEntity
		return repo.findById( id, Y_MemberForMe.class );
	}

	public Y_MemberForPublic getInfoForPublic ( Long id ) throws NoSuchEntity {
		// TODO NoSuchEntity
		return repo.findById( id, Y_MemberForPublic.class );
	}

	@Transactional
	public Member edit ( Long orgId , Z_MemberEdit req ) throws NoSuchEntity {

		Member org = repo.findById( orgId )
		        .orElseThrow( NoSuchEntity::new );

		Member updata = req.toEntity();
		org.update( updata );
		return org;
	}
}
