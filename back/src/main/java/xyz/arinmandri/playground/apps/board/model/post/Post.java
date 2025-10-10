package xyz.arinmandri.playground.apps.board.model.post;

import xyz.arinmandri.playground.apps.a.model.BaseEntityWithId;

import java.util.ArrayList;
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

	@OneToMany( mappedBy = "belongsTo" , cascade = CascadeType.ALL , orphanRemoval = true )
	@OrderBy( "order ASC" )
	List<PAttachmentImage> attachmentsImage;

	@OneToMany( mappedBy = "belongsTo" , cascade = CascadeType.ALL , orphanRemoval = true )
	@OrderBy( "order ASC" )
	List<PAttachmentFile> attachmentsFile;

	Post(
	        PAuthor author ,
	        String content ,
	        List<PAttachment> attachments
	) {
		this.author = author;
		this.content = content;

		if( attachments == null ) attachments = new ArrayList<>();
		this.attachments = attachments;

		this.attachmentsImage = attachments.stream()
		        .filter( p-> p.getType().equals( PAttachmentImage.TYPE ) )
		        .map( p-> (PAttachmentImage) p )
		        .toList();
		this.attachmentsFile = attachments.stream()
		        .filter( p-> p.getType().equals( PAttachmentFile.TYPE ) )
		        .map( p-> (PAttachmentFile) p )
		        .toList();
	}

	/*
	 * 자동생성 Builder 때문에 어쩔 수 없이 있는 겨.
	 * 하위타입을 빌더에 넣지 마시오.
	 * 라고 어딘가에 말하고싶다.
	 */
	Post(
	        PAuthor author ,
	        String content ,
	        List<PAttachment> attachments ,
	        List<PAttachmentImage> attachmentsImage ,
	        List<PAttachmentFile> attachmentsFile
	) {
		this( author, content, attachments );
	}

	public void setAttachments ( List<PAttachment> attachments ) {

		this.attachments = attachments;

		this.attachmentsImage = attachments.stream()
		        .filter( p-> p.getType().equals( PAttachmentImage.TYPE ) )
		        .map( p-> (PAttachmentImage) p )
		        .toList();
		this.attachmentsFile = attachments.stream()
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
