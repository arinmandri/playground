package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.model.post.PAttachmentImage;

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
