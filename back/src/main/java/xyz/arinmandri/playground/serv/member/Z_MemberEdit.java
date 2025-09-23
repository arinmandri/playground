package xyz.arinmandri.playground.serv.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Z_MemberEdit
{
	String nick;

	String email;

	@Setter
	String propic;
}