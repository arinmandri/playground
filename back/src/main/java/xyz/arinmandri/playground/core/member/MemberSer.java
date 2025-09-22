package xyz.arinmandri.playground.core.member;

import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;
import xyz.arinmandri.playground.core.authedmember.AuthenticatedMember;
import xyz.arinmandri.playground.core.authedmember.AuthenticatedMemberRepo;
import xyz.arinmandri.playground.core.authedmember.MKeyBasic;
import xyz.arinmandri.playground.core.authedmember.MKeyBasicRepo;
import xyz.arinmandri.playground.core.authedmember.Z_MKeyBasicAdd;

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
	final private AuthenticatedMemberRepo athmRepo;
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

	/**
	 * 생성: @link{Member} + @link{MKeyBasic}
	 */
	@Transactional
	public Long addMemberWithKeyBasic ( Z_MemberAdd memberReq , Z_MKeyBasicAdd keyReq ) throws UniqueViolated {
		Member member = memberReq.toEntity();
		repo.save( member );

		AuthenticatedMember athm = AuthenticatedMember.builder()
		        .id( member.getId() )
		        .build();
		MKeyBasic mkey = keyReq.toEntity( athm );

		athmRepo.save( athm );
		MKeyBasic result = mkeyBasicRepo.save( mkey );
		mkeyBasicRepo.flush();
		return result.getId();
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
