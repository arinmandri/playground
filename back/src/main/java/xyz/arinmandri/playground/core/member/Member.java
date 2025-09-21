package xyz.arinmandri.playground.core.member;

import xyz.arinmandri.playground.core.BaseEntityWithId;

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
public class Member extends BaseEntityWithId
{

	@Column( nullable = false , length = 20 )
	private String nick;

	@Column( length = 100 )
	private String email;

	@Column( length = 256 )
	private String propic;

	void update ( Member data ) {
		if( data.nick != null ){
			if( data.nick.equals( "" ) )
			    nick = null;
			else
			    nick = data.nick;
		}
		if( data.email != null ){
			if( data.email.equals( "" ) )
			    email = null;
			else
			    email = data.email;
		}
		if( data.propic != null ){
			if( data.propic.equals( "" ) )
			    propic = null;
			else
			    propic = data.propic;
		}
	}
}
