package xyz.arinmandri.playground.core.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.arinmandri.playground.core.BaseEntity;

@Entity
@Table( schema = "playground" )
/*
 * 다른 엔터티들이 member에 의존; member은 다른 엔터티에 의존 X
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseEntity
{

	@Column( nullable = false , length = 20 )
	private String nick;

	@Column( length = 100 )
	private String email;

	@Column( length = 256 )
	private String propic;

	void update ( Member data ) {
		if( data.nick != null ) nick = data.nick;
		if( nick.equals( "" ) ) nick = null;
		if( data.email != null ) email = data.email;
		if( email.equals( "" ) ) email = null;
		if( data.propic != null ) propic = data.propic;
		if( propic.equals( "" ) ) propic = null;
	}
}
