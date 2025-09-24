package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAttachmentImage;

import lombok.Getter;
import lombok.Setter;


public class Z_PAttachmentAddImage extends Z_PAttachmentAdd
{

	public static final String type = "image";// TODO 도메인에서가져와?

	@Getter
	@Setter
	String url;

	@Override
	public PAttachmentImage toEntity () {
		return PAttachmentImage.builder()
		        .url( url )
		        .build();
	}
}
