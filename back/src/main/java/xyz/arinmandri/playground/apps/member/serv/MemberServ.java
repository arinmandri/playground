package xyz.arinmandri.playground.apps.member.serv;

import xyz.arinmandri.playground.apps.a.serv.PersistenceServ;
import xyz.arinmandri.playground.apps.a.serv.exception.NoSuchEntity;
import xyz.arinmandri.playground.apps.member.domain.MKeyBasic;
import xyz.arinmandri.playground.apps.member.domain.Member;
import xyz.arinmandri.playground.apps.member.domain.Members;

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
public class MemberServ extends PersistenceServ
{

	final private Members members;

	public Y_MemberForMe getInfoForMe ( Long id ) throws NoSuchEntity {
		// TODO NoSuchEntity
		return members.findById( Y_MemberForMe.class, id );
	}

	public Y_MemberForPublic getInfoForPublic ( Long id ) throws NoSuchEntity {
		// TODO NoSuchEntity
		return members.findById( Y_MemberForPublic.class, id );
	}

	/**
	 * 생성: @link{Member} + @link{MKeyBasic}
	 * 
	 * @return member.id
	 */
	@Transactional
	public Long addMemberWithKeyBasic ( Z_MemberAdd memberReq , Z_MKeyBasicAdd keyReq ) {

		Member m = memberReq.toEntity();

		MKeyBasic mkey = keyReq.toEntity( m );

		return members.add( m, mkey );
	}

	@Transactional
	public void edit ( Long orgId , Z_MemberEdit req ) throws NoSuchEntity {

		Member org = members.findById( Member.class, orgId );
		Member updata = Z_MemberEdit_toEntity( req, org );
		members.edit( orgId, updata );
	}

	private Member Z_MemberEdit_toEntity ( Z_MemberEdit req , Member org ) {
		// XXX 이걸 어떻게 공통으로...
		return Member.builder()
		        .nick( req.nick != null ? req.nick : org.getNick() )
		        .email( req.email == null
		                ? org.getEmail()
		                : req.email.equals( "" )
		                        ? null
		                        : req.email )
		        .propic( req.propic == null
		                ? org.getPropic()
		                : req.propic.equals( "" )
		                        ? null
		                        : req.propic )
		        .build();
	}
}
