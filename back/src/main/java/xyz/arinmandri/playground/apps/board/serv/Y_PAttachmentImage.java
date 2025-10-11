package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.model.post.PAttachmentType;

public interface Y_PAttachmentImage extends Y_PAttachment
{
	@Override
	default PAttachmentType getType () {
		return PAttachmentType.image;
	}

	String getUrl ();
}
