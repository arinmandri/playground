package xyz.arinmandri.playground.mkey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberRepo;
import xyz.arinmandri.playground.core.member.MemberSer.AddReq;


@Service
@RequiredArgsConstructor
public class MkeySer implements UserDetailsService
{
	@Value( "${jwt.expia}" )
	private long expiA;
	@Value( "${jwt.expiag}" )
	private long expiAG;
	@Value( "${jwt.expir}" )
	private long expiR;

	final private MemberRepo memberRepo;
	final private MkeyBasicRepo mkeyBasicRepo;

	@Override
	public UserDetails loadUserByUsername ( String key ) throws UsernameNotFoundException {
		UserDetails u;
		u = mkeyBasicRepo.findByKeyname( key ).orElse( null );
		return u;
	}

	@Transactional
	public MkeyBasic addMemberWithKeyBasic ( AddBasicWithMemberReq req ) {
		// TODO 중복
		MkeyBasic findExisting = mkeyBasicRepo.findByKeyname( req.key.keyname )
		        .orElse( null );

		try{
			Member member = req.member.toEntity();
			MkeyBasic mkey = req.key.toEntity( member );
			memberRepo.save( member );
			return mkeyBasicRepo.save( mkey );
		}
		catch( Exception e ){
			throw e;// TODO exception
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

		public MkeyBasic toEntity ( Member owner ) {
			return MkeyBasic.builder()
			        .owner( owner )
			        .keyname( keyname )
			        .password( password )
			        .build();
		}
	}
}
