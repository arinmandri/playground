package xyz.arinmandri.playground.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;


@Getter
public class UserGuest extends User
{
	private static final long serialVersionUID = 1_000_000L;

	String id;

	public UserGuest( Collection<? extends GrantedAuthority> authorities , String id ) {
		super( authorities );
		this.id = id;
	}

	@Override
	public Type getType () {
		return Type.guest;
	}

	@Override
	public String getUsername () {
		// TODO Auto-generated method stub
		return null;
	}
}
