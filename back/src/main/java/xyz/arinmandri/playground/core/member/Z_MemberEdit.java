package xyz.arinmandri.playground.core.member;

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

	Member toEntity () {
		return Member.builder()
		        .nick( nick )
		        .email( email )
		        .propic( propic )
		        .build();
	}
}