package xyz.arinmandri.playground.core.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

	String email;// TODO email 형식

	@Setter
	String propic;

	public Member toEntity () {

		return Member.builder()
		        .nick( nick.equals( "" ) ? null : nick )
		        .email( email == null || email.equals( "" ) ? null : email )
		        .propic( propic == null || propic.equals( "" ) ? null : propic )
		        .build();
	}
}
