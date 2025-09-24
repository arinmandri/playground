package xyz.arinmandri.playground.serv.board;

import lombok.Getter;


@Getter
public class Z_PAttachmentNew extends Z_PAttachment
{
	public static final String TYPE = "new";

	@Override
	public String getType () {
		return TYPE;
	}

	Z_PAttachmentAdd content;
}
