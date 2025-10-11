package xyz.arinmandri.playground.apps.board.model.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( name = "p_attachment_image" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PAttachmentImage extends PAttachment
{
	@Override
	public PAttachmentType getType () {
		return PAttachmentType.image;
	}

	@Column( nullable = false )
	String url;
}
