package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAttachmentImage;

import lombok.Getter;
import lombok.Setter;


public class Z_PAttachmentAddImage extends Z_PAttachmentAdd
{
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
