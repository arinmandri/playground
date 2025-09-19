package xyz.arinmandri.playground.core.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity( name = "p_attachment_image" )
@Table( schema = "playground" , name = "p_attachment_image" )
@NoArgsConstructor
@AllArgsConstructor
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
