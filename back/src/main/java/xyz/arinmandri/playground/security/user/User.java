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

	public enum Type
	{
		guest,
		normal,
		;
	}

	public abstract Type getType ();

	static public String composeUserId ( Type userType , String userName ) {
		String userId = userType.toString() + ":" + userName;
		return userId;
	}

	static public User parseUserId ( String userId , Collection<? extends GrantedAuthority> authorities ) {

		String[] parts = userId.split( ":" );
		User.Type type;
		try {
			type = User.Type.valueOf( parts[0] );
		}
		catch( IllegalArgumentException e ){
			// TODO exception 없는 타입
			throw e;
		}
		switch( type ){
		case guest:
			return new UserGuest( authorities, parts[1] );
		case normal:
			return new UserNormal( authorities, Long.valueOf( parts[1] ) );
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
