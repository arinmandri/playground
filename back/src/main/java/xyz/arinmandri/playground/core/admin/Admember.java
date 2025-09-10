package xyz.arinmandri.playground.core.admin;

import xyz.arinmandri.playground.core.BaseEntity;
import xyz.arinmandri.playground.core.Loginable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table( schema = "playground" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Admember extends BaseEntity
        implements Loginable
{

	@Column( nullable = false , unique = true )
	private String keyname;

	@Column( nullable = false )
	private String password;

	@Column( nullable = false , length = 20 )
	private String nick;

	@Column( length = 100 )
	private String email;

	@Column( length = 256 )
	private String propic;

	@Override
	public int getLoginableType () {
		return Loginable.OWNER_TYPE_MEMBER;
	}
}
