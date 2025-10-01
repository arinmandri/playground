package xyz.arinmandri.playground.apps.member.domain;

import xyz.arinmandri.playground.apps.a.domain.BaseEntityWithId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table( name="member" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseEntityWithId
{

	@Column( nullable = false , length = 20 )
	private String nick;

	@Column( length = 100 )
	private String email;

	@Column( length = 256 )
	private String propic;

	void setId ( Long id ) {
		this.id = id;
	}
}
