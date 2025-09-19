package xyz.arinmandri.playground.core.board;

public interface Y_PAttachmentImage extends Y_PAttachment
{
	@Override
	default String getType () {
		return "image";
	}

	String getUrl ();
}
