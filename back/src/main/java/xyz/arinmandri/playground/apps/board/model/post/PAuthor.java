package xyz.arinmandri.playground.apps.board.model.post;

import xyz.arinmandri.playground.apps.member.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Immutable
@Table( name = "member" )
/**
 * @see xyz.arinmandri.playground.apps.member.model.Member
 *      read-only
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PAuthor
{
	@Id
	Long id;

	@Column( updatable = false )
	private String nick;

	@Column( updatable = false )
	private String propic;

	static public PAuthor from ( Member m ) {
		return builder()
		        .id( m.getId() )
		        .nick( m.getNick() )
		        .propic( m.getPropic() )
		        .build();
	}
}
