package xyz.arinmandri.playground.core.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table( schema = "playground" , name = "p_att_image" )
public class PAttImage extends PAttachment
{

	@Column( nullable = false )
	String url;
}
