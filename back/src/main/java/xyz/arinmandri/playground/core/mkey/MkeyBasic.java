package xyz.arinmandri.playground.core.mkey;

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


@Entity
@Table( schema = "playground" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MkeyBasic extends BaseEntity
{
	@JoinColumn( name = "owner__m" , nullable = false )
	@OneToOne
	private Member owner;

	@Column( nullable = false , unique = true )
	private String keyname;

	@Column( nullable = false )
	private String password;

}
