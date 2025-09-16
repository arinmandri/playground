package xyz.arinmandri.playground.core.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


/**
 * TEST
 */
@Entity
@Table( schema = "playground" , name = "p_att_ex1" )
public class PAttEx1 extends PAttachment
{

	@Column( nullable = false )
	String text1;

	@Column( nullable = false )
	Integer int1;
}
