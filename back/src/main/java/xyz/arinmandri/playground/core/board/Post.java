package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.BaseEntityWithId;
import xyz.arinmandri.playground.core.member.Member;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Post extends BaseEntityWithId
{
	@Id
	@Column( updatable = false )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;

	@JoinColumn( name = "author__m" )
	@ManyToOne
	private Member author;

	@Column( nullable = false )
	private String content;

	@OneToMany( mappedBy = "post" , cascade = CascadeType.ALL )
	private List<PAttachment> attachments;

	void update ( Post data ) {
		if( data.content != null ) content = data.content;
	}
}
