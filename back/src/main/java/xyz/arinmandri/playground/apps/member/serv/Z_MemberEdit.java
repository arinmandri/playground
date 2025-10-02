package xyz.arinmandri.playground.apps.member.serv;

import xyz.arinmandri.playground.apps.a.serv.Temp;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Z_MemberEdit
{
	String nick;

	@Pattern( regexp = Temp.EMAIL_REGEX )
	String email;

	@Setter
	String propic;
}