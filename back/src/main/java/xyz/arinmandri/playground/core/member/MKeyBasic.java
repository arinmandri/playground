package xyz.arinmandri.playground.core.member;

import xyz.arinmandri.playground.core.BaseEntityWithId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( name = "mkey_basic" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MKeyBasic extends BaseEntityWithId
{
	@JoinColumn( name = "belongs_to__m" , nullable = false )
	@OneToOne
	private Member owner;

	@Column( nullable = false , unique = true )
	private String keyname;

	@Column( nullable = false )
	private String password;

}
