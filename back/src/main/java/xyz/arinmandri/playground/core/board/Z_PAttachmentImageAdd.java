package xyz.arinmandri.playground.core.board;

import lombok.Getter;
import lombok.Setter;


public class Z_PAttachmentImageAdd extends Z_PAttachmentAdd
{

	public static final String type = "image";// TODO 도메인에서가져와?

	@Getter
	@Setter
	String url;

	@Override
	PAttachmentImage toEntity () {
		return PAttachmentImage.builder()
		        .url( url )
		        .build();
	}
}
