package xyz.arinmandri.playground.core.member;

import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;


/**
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

	public Member get ( long id ) throws NoSuchEntity {
		return repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Member.class, id ) );
	}

	/**
	 * 생성: @link{Member} + @link{MKeyBasic}
	 */
	@Transactional
	public MKeyBasic addMemberWithKeyBasic ( Z_MemberAdd memberReq , Z_MKeyBasicAdd keyReq ) throws UniqueViolated {
		Member member = memberReq.toEntity();
		MKeyBasic mkey = keyReq.toEntity( member );

		try{
			repo.save( member );
			MKeyBasic result = mkeyBasicRepo.save( mkey );

			mkeyBasicRepo.flush();
			return result;
		}
		catch( DataIntegrityViolationException e ){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// XXX 이걸 실행하지 않으면 내 커스텀 에러가 컨트롤러에 전달되지 않고 스프링이 자동으로 (cause도 없는) 다른 에러로 바꿔버린다. 아마 @Transactional 이 붙었고 내 커스텀 에러를 던지는 모든 메서드에서 이같이 하도록 애스팩트를 만들 수 있을 거 같다.
			maybeThrowsUniqueViolated( e, "mkey_basic_keyname_key", "email duplicate." );
			throw e;
		}
	}

	@Transactional
	public Member edit ( Long orgId , Z_MemberEdit req ) throws NoSuchEntity {

		Member org = mkeyBasicRepo.findByOwnerId( orgId )
		        .orElseThrow( NoSuchEntity::new ).getOwner();

		Member updata = req.toEntity();
		org.update( updata );
		return org;
	}
}
