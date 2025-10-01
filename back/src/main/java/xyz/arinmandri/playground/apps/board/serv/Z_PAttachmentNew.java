package xyz.arinmandri.playground.apps.board.serv;

import lombok.Getter;


@Getter
public class Z_PAttachmentNew extends Z_PAttachmentNoo
{
	public static final String TYPE = "new";

	@Override
	public String getType () {
		return TYPE;
	}

	Z_PAttachmentAdd content;
}
