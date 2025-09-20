package xyz.arinmandri.playground.core.board;

import lombok.With;


public record Z_PAttachmentFileAdd (
        @With String url
) implements Z_PAttachmentAdd {
	public static final String type = "file";// TODO 도메인에서가져와?

	@Override
	public PAttachmentFile toEntity () {
		return PAttachmentFile.builder()
		        .url( url )
		        .size( 10 )// TODO
		        .build();
	}
}