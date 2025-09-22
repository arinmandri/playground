package xyz.arinmandri.playground.core.board.post;

import xyz.arinmandri.playground.core.BaseEntityWithId;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( name = "post" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Post extends BaseEntityWithId
{

	@JoinColumn( name = "author__m" )
	@ManyToOne
	private PAuthor author;

	@Column( nullable = false )
	private String content;

	@OneToMany( mappedBy = "belongsTo" )
	private List<PAttachment> attachments;

	@OneToMany( mappedBy = "belongsTo" )
	private List<PAttachmentImage> attachmentsImage;

	@OneToMany( mappedBy = "belongsTo" )
	private List<PAttachmentFile> attachmentsFile;

	public void update ( Post data ) {
		if( data.content != null ) content = data.content;
	}

	public void setAttachments ( List<PAttachment> list ) {
		this.attachments = list;

		int order = 1;
		List<PAttachmentImage> listImage = new ArrayList<>();
		List<PAttachmentFile> listFile = new ArrayList<>();
		for( PAttachment item : list ){
			item.setOrder( order++ );
			item.setBelongsTo( this );

			if( item instanceof PAttachmentImage itemImage )
			    listImage.add( itemImage );
			if( item instanceof PAttachmentFile itemFile )
			    listFile.add( itemFile );
		}
		this.attachmentsImage = listImage;
		this.attachmentsFile = listFile;
	}
}
