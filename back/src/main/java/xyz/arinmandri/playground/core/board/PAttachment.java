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


@Entity( name = "p_attachment" )
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@NoArgsConstructor
@Getter
public abstract class PAttachment extends BaseEntityWithoutId
{
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE )
	@SequenceGenerator(
	        name = "playground.pattachment_seq" ,
	        sequenceName = "playground.pattachment_seq" ,
	        schema = "playground" ,// 스키마 명시했는데 Hibernate가 `select nextval('pattachment_seq')`라고 찾음 ㅡㅡ
	        allocationSize = 10// XXX 아니 진짜 뭐냐 버그인가? 설정이 안 된다. org.hibernate.MappingException: The increment size of the [pattachment_seq] sequence is set to [50] in the entity mapping while the associated database sequence increment size is [{내가 DB에서 만든 시퀀스의 INCREMENT BY 값}]. 어느 다른 수를 적어도 50이라고만 한다. 결국에는 DB에서의 저 값을 50으로 맞췄다.
	)
	private Long id;

	@JoinColumn( name = "belongs_to__p" )
	@ManyToOne( targetEntity = Post.class )
	@Setter
	private Post belongsTo;

	@Column
	@Setter
	private Integer order;

	public abstract String getType ();
}
