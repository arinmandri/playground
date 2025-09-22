package xyz.arinmandri.playground.core.authedmember;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class Z_MKeyBasicAdd
{
	// XXX 제한 추가. 길이라든가 정규식 뭐 있겠지.
	@NotNull
	String keyname;

	@NotNull
	@Setter
	String password;

	public MKeyBasic toEntity ( AuthenticatedMember owner ) {
		return MKeyBasic.builder()
		        .owner( owner )
		        .keyname( keyname )
		        .password( password )
		        .build();
	}
}