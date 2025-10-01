package xyz.arinmandri.playground.apps.member.serv;

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