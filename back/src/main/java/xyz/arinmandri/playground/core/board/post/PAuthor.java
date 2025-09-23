package xyz.arinmandri.playground.core.board.post;

import xyz.arinmandri.playground.core.BaseEntityWithoutId;
import xyz.arinmandri.playground.core.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( name = "member" )
/**
 * @See xyz.arinmandri.playground.core.authedmember.Member
 *      read-only
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PAuthor extends BaseEntityWithoutId
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
