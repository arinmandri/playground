package xyz.arinmandri.playground.core.mkey;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.BaseEntity.ConstraintDesc;
import xyz.arinmandri.playground.core.PersistenceSer;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberRepo;
import xyz.arinmandri.playground.core.member.MemberSer.AddReq;


@Service
@RequiredArgsConstructor
public class MkeySer extends PersistenceSer
{
	final private PasswordEncoder pwEncoder;
	
	final private MemberRepo memberRepo;
	final private MkeyBasicRepo mkeyBasicRepo;

	@Transactional
	public MkeyBasic addMemberWithKeyBasic ( AddBasicWithMemberReq req ) throws UniqueViolated {

		try{
			Member member = req.member.toEntity();
			MkeyBasic mkey = req.key.toEntity( member, pwEncoder );
			memberRepo.save( member );
			MkeyBasic result = mkeyBasicRepo.save( mkey );

			mkeyBasicRepo.flush();
			return result;
		}
		catch( DataIntegrityViolationException e ){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// XXX 이걸 실행하지 않으면 내 커스텀 에러가 컨트롤러에 전달되지 않고 스프링이 자동으로 (cause도 없는) 다른 에러로 바꿔버린다. 아마 @Transactional 이 붙었고 내 커스텀 에러를 던지는 모든 메서드에서 이같이 하도록 애스팩트를 만들 수 있을 거 같다.
			maybeThrowsUniqueViolated( e, ConstraintDesc.MkeyBasic_Email );
			throw e;
		}
	}

	@AllArgsConstructor
	@Getter
	static public class AddBasicWithMemberReq
	{
		AddReq member;
		AddBasicReq key;
	}

	@AllArgsConstructor
	@Getter
	static public class AddBasicReq
	{
		String keyname;
		String password;

		public MkeyBasic toEntity ( Member owner , PasswordEncoder pwEncoder ) {
			return MkeyBasic.builder()
			        .owner( owner )
			        .keyname( keyname )
			        .password( pwEncoder.encode( password) )
			        .build();
		}
	}
}
