package xyz.arinmandri.playground.apps.board.serv;

public interface Y_PAttachmentImage extends Y_PAttachment
{
	@Override
	default String getType () {
		return "image";
	}

	String getUrl ();
}
