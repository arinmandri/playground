package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.domain.post.PAttachmentFile;

import lombok.Getter;
import lombok.Setter;


public class Z_PAttachmentAddFile extends Z_PAttachmentAdd
{
	@Getter
	@Setter
	String url;

	@Setter
	Integer size;

	@Override
	public PAttachmentFile toEntity () {
		return PAttachmentFile.builder()
		        .url( url )
		        .size( size )
		        .build();
	}
}
