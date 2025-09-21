package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.BaseEntityWithoutId;

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
 * @See xyz.arinmandri.playground.core.member.Member
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

	@Column
	private String nick;

	@Column
	private String propic;
}
