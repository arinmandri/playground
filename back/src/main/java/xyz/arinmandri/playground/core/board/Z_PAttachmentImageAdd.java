package xyz.arinmandri.playground.core.board;

import lombok.With;


public record Z_PAttachmentImageAdd (
        @With String url
) implements Z_PAttachmentAdd {

	public static final String type = "image";// TODO 도메인에서가져와?

	@Override
	public PAttachmentImage toEntity () {
		return PAttachmentImage.builder()
		        .url( url )
		        .build();
	}
}
