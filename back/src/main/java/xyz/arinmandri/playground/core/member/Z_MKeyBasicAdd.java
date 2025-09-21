package xyz.arinmandri.playground.core.member;

import jakarta.validation.constraints.NotNull;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class Z_MKeyBasicAdd
{
	// XXX 제한 추가. 길이라든가 정규식 뭐 있겠지.
	@NotNull
	String keyname;
	@NotNull
	String password;

	public MKeyBasic toEntity ( Member owner , PasswordEncoder pwEncoder ) {
		return MKeyBasic.builder()
		        .owner( owner )
		        .keyname( keyname )
		        .password( pwEncoder.encode( password ) )
		        .build();
	}
}