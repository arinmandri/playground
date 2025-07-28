package xyz.arinmandri.playground.core.mkey;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.arinmandri.playground.core.BaseEntity;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.TokenProvider;


@Entity
@Table( schema = "playground" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MkeyBasic extends BaseEntity
        implements UserDetails
{
	private static final long serialVersionUID = 1_000_000L;

	@JoinColumn( name = "owner__m" , nullable = false )
	@OneToOne
	private Member owner;

	@Column( nullable = false , unique = true )
	private String keyname;

	@Column( nullable = false )
	private String password;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return List.of( ()-> TokenProvider.normalAuthority );
	}

	@Override
	public String getPassword () {
		return password;
	}

	@Override
	public String getUsername () {
		// TODO Auto-generated method stub
		return null;
	}
}
