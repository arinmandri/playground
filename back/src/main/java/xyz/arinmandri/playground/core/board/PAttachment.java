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


@Entity
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@Getter
public abstract class PAttachment extends BaseEntityWithoutId
{
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE )
	@SequenceGenerator( name = "pattachment_seq" , sequenceName = "pattachment_seq" , schema = "playgrou nd", allocationSize = 1)
	private Long id;

	@JoinColumn( name = "owner__p" )
	@ManyToOne
	private Post post;

	@Column
	private Integer order;

	public abstract String getType ();
}
