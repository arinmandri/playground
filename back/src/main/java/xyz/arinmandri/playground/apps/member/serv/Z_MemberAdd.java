package xyz.arinmandri.playground.apps.member.serv;

import xyz.arinmandri.playground.apps.a.serv.Temp;
import xyz.arinmandri.playground.apps.member.domain.Member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Z_MemberAdd
{
	@NotNull
	@NotBlank
	String nick;

	@Pattern( regexp = Temp.EMAIL_REGEX )
	String email;

	@Setter
	String propic;

	Member toEntity () {

		return Member.builder()
		        .nick( nick.equals( "" ) ? null : nick )
		        .email( email == null || email.equals( "" ) ? null : email )
		        .propic( propic == null || propic.equals( "" ) ? null : propic )
		        .build();
	}
}
