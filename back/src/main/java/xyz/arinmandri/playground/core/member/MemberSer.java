package xyz.arinmandri.playground.core.member;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.BaseEntity.ConstraintDesc;
import xyz.arinmandri.playground.core.NoSuchEntity;
import xyz.arinmandri.playground.core.PersistenceSer;


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
	final private PasswordEncoder pwEncoder;

	final private MKeyBasicRepo mkeyBasicRepo;

	public Member get ( long id ) throws NoSuchEntity{
		return repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Member.class, id ) );
	}

	/**
	 * 생성: @link{Member} + @link{MKeyBasic}
	 */
	@Transactional
	public MKeyBasic addMemberWithKeyBasic (
	        AddMemberReq memberReq ,
	        AddMKeyBasicReq keyReq ) throws UniqueViolated{

		try{
			Member member = memberReq.toEntity();
			MKeyBasic mkey = keyReq.toEntity( member, pwEncoder );
			repo.save( member );
			MKeyBasic result = mkeyBasicRepo.save( mkey );

			mkeyBasicRepo.flush();
			return result;
		}
		catch( DataIntegrityViolationException e ){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// XXX 이걸 실행하지 않으면 내 커스텀 에러가 컨트롤러에 전달되지 않고 스프링이 자동으로 (cause도 없는) 다른 에러로 바꿔버린다. 아마 @Transactional 이 붙었고 내 커스텀 에러를 던지는 모든 메서드에서 이같이 하도록 애스팩트를 만들 수 있을 거 같다.
			maybeThrowsUniqueViolated( e, ConstraintDesc.MkeyBasic_Email );
			throw e;
		}
	}

	static public record AddMKeyBasicReq(
	        String keyname ,
	        String password )
	{

		public MKeyBasic toEntity ( Member owner , PasswordEncoder pwEncoder ){
			return MKeyBasic.builder()
			        .owner( owner )
			        .keyname( keyname )
			        .password( pwEncoder.encode( password ) )
			        .build();
		}
	}

	@Transactional
	public Member edit ( Long id , EditMemberReq req ) throws NoSuchEntity{
		Member m = repo.findById( id )
		        .orElseThrow( ()-> new NoSuchEntity( Member.class, id ) );
		m.update( req.toEntity() );
		return m;
	}

	static public record AddMemberReq(
	        String nick ,
	        String email ,
	        String propic )
	{

		public Member toEntity (){

			return Member.builder()
			        .nick( nick.equals( "" ) ? null : nick )
			        .email( email.equals( "" ) ? null : email )
			        .propic( propic.equals( "" ) ? null : propic )
			        .build();
		}
	}

	@AllArgsConstructor
	@Getter
	static public class EditMemberReq
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
