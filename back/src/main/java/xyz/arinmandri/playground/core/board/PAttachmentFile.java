package xyz.arinmandri.playground.core.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity( name = "p_attachment_file" )
@Table( schema = "playground" , name = "p_attachment_file" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PAttachmentFile extends PAttachment
{

	@Override
	public String getType () {
		return "file";
	}

	@Column( nullable = false )
	String url;

	@Column( nullable = false )
	@Setter
	Integer size;
}
