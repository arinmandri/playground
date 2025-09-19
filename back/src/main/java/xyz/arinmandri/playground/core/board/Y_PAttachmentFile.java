package xyz.arinmandri.playground.core.board;

public interface Y_PAttachmentFile extends Y_PAttachment
{
	@Override
	default String getType () {
		return "file";
	}

	String getUrl ();

	Integer getSize ();
}
