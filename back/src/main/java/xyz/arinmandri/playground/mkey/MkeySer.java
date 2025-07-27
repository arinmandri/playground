package xyz.arinmandri.playground.mkey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


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

	final private MkeyBasicRepo MemberBKRepo;

	@Override
	public UserDetails loadUserByUsername ( String key ) throws UsernameNotFoundException {
		UserDetails u;
		u = MemberBKRepo.findByKeyname( key ).orElse( null );
		return u;
	}
}
