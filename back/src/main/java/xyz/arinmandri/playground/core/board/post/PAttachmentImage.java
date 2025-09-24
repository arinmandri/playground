package xyz.arinmandri.playground.core.board.post;

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
	public static final String TYPE = "image";

	@Override
	public String getType () {
		return "image";
	}

	@Column( nullable = false )
	String url;
}
