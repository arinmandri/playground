package xyz.arinmandri.playground.apps.board.model.post;

import xyz.arinmandri.playground.apps.a.model.BaseEntityWithId;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table( name = "post" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
// XXX 저놈의 상속관계 엔터티 때문에 그냥 어노테이션 띡 붙이는 걸로는 엔터티의 무결함이 유지가 안 되고 그걸 해결하려면 어노테이션 빼고 장황한 코드를 적어야 한다. 돌겠 일단은 Posts.processPAttachments 돌리면 attachments 필드를 기준으로 다시 잡게 함. 그러니까 그냥 Post의 setter만 써서는 와장창 된다는 말이다.
public class Post extends BaseEntityWithId
{

	@JoinColumn( name = "author__m" , updatable = false )
	@ManyToOne
	PAuthor author;

	@Column( nullable = false )
	String content;

	@OneToMany( mappedBy = "belongsTo" , cascade = CascadeType.ALL , orphanRemoval = true )
	@OrderBy( "order ASC" )
	@Setter
	List<PAttachment> attachments;

	@OneToMany( mappedBy = "belongsTo" , cascade = CascadeType.ALL , orphanRemoval = true )
	@OrderBy( "order ASC" )
	List<PAttachmentImage> attachmentsImage;

	@OneToMany( mappedBy = "belongsTo" , cascade = CascadeType.ALL , orphanRemoval = true )
	@OrderBy( "order ASC" )
	List<PAttachmentFile> attachmentsFile;

	public void update ( Post data ) {
		if( data.content != null ) content = data.content;
	}

	void setId ( Long id ) {
		this.id = id;
	}
}
