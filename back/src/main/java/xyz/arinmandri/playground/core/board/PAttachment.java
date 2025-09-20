package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.BaseEntityWithoutId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@NoArgsConstructor
@Getter
abstract class PAttachment extends BaseEntityWithoutId
{
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "p_attachment_seq" )
	@SequenceGenerator(
	        name = "p_attachment_seq",
	        sequenceName = "p_attachment_seq",
	        allocationSize = 10
	)
	private Long id;

	@JoinColumn( name = "belongs_to__p" )
	@ManyToOne
	@Setter
	private Post belongsTo;

	@Column
	@Setter
	private Integer order;

	public abstract String getType ();
}
