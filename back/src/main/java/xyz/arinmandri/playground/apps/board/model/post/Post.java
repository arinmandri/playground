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

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( name = "post" )
@NoArgsConstructor
@Builder
@Getter
public class Post extends BaseEntityWithId
{
	@JoinColumn( name = "author__m" , updatable = false )
	@ManyToOne
	PAuthor author;

	@Column( nullable = false )
	String content;

	@OneToMany( mappedBy = "belongsTo" , cascade = CascadeType.ALL , orphanRemoval = true )
	@OrderBy( "order ASC" )
	List<PAttachment> attachments;

	public Post( PAuthor author , String content , List<PAttachment> attachments ) {
		this.author = author;
		this.content = content == null ? "" : content;
		this.attachments = attachments == null ? List.of() : attachments;

		int order = 0;
		for( PAttachment att : attachments ){
			att.setOrder( order++ );
			att.setBelongsTo( this );
		}
	}

	@Column
	public List<PAttachmentImage> getAttachmentsImage () {
		return attachments.stream()
		        .filter( p-> p.getType().equals( PAttachmentImage.TYPE ) )
		        .map( p-> (PAttachmentImage) p )
		        .toList();
	}

	@Column
	public List<PAttachmentFile> getAttachmentsFile () {
		return attachments.stream()
		        .filter( p-> p.getType().equals( PAttachmentFile.TYPE ) )
		        .map( p-> (PAttachmentFile) p )
		        .toList();
	}

	public void update ( Post data ) {
		if( data.content != null ) content = data.content;
	}

	void setId ( Long id ) {
		this.id = id;
	}
}
