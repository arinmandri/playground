package xyz.arinmandri.playground.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;


@Getter
public class UserNormal extends User
{
	private static final long serialVersionUID = 1_000_000L;

	final Long id;

	public UserNormal( Collection<? extends GrantedAuthority> authorities , Long id ) {
		super( authorities );
		this.id = id;
	}

	@Override
	public Type getType () {
		return Type.normal;
	}

	@Override
	public String getUsername () {
		// TODO Auto-generated method stub
		return null;
	}
}
