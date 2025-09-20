package xyz.arinmandri.playground.core.board;

import lombok.Getter;
import lombok.Setter;


public class Z_PAttachmentFileAdd extends Z_PAttachmentAdd
{
	public static final String type = "file";// TODO 도메인에서가져와?

	@Getter
	@Setter
	String url;

	@Setter
	Integer size;

	@Override
	PAttachmentFile toEntity () {
		return PAttachmentFile.builder()
		        .url( url )
		        .size( size )
		        .build();
	}
}