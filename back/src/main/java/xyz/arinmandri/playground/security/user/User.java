package xyz.arinmandri.playground.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public abstract class User
        implements UserDetails
{
	private static final long serialVersionUID = 1_000_000L;

	final Collection<? extends GrantedAuthority> authorities;

	public User( Collection<? extends GrantedAuthority> authorities ) {
		this.authorities = authorities;
	}

	enum Type
	{
		guest,
		normal,
		;
	}

	public abstract Type getType ();

	static public User from ( UserDetails userDetails ) {
		if( userDetails == null ) return null;

		String[] parts = userDetails.getUsername().split( ":" );
		switch( parts[0] ){
		case "guest":
			return new UserGuest( userDetails.getAuthorities(), parts[1] );
		case "member":// XXX member: member가 normal 말고 다른 타입이 생길까?
			return new UserNormal( userDetails.getAuthorities(), Long.valueOf( parts[1] ) );
		}
		throw new RuntimeException();// TODO exception
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return authorities;
	}

	@Override
	public String getPassword () {
		return null;
	}
}
