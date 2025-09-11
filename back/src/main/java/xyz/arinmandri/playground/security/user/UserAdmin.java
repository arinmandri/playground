package xyz.arinmandri.playground.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;


@Getter
public class UserAdmin extends User
{
	private static final long serialVersionUID = 1_000_000L;

	final Long admemberId;

	public UserAdmin( Collection<? extends GrantedAuthority> authorities , Long id ) {
		super( authorities );
		this.admemberId = id;
	}

	@Override
	public Type getType () {
		return Type.admin;
	}

	@Override
	public String getCode () {
		return String.valueOf(admemberId );
	}
}
