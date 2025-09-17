package xyz.arinmandri.playground.core.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;


@Entity
@Table( schema = "playground" , name = "p_attachment_image" )
@Builder
@Getter
public class PAttachmentImage extends PAttachment
{

	@Override
	public String getType () {
		return "image";
	}

	@Column( nullable = false )
	String url;
}
