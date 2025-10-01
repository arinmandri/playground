package xyz.arinmandri.playground.apps.board.serv;

public interface Y_PAttachmentFile extends Y_PAttachment
{
	@Override
	default String getType () {
		return "file";
	}

	String getUrl ();

	Integer getSize ();
}
