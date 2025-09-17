package xyz.arinmandri.playground.core.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;


@Entity
@Table( schema = "playground" , name = "p_att_file" )
@Builder
@Getter
public class PAttFile extends PAttachment
{

	@Override
	public String getType () {
		return "file";
	}

	@Column( nullable = false )
	String url;

	@Column( nullable = false )
	Integer size;
}
