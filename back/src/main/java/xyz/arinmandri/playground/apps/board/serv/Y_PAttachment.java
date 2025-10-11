package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.model.post.PAttachmentType;


public interface Y_PAttachment
{
	Long getId ();

	PAttachmentType getType ();

	Integer getOrder ();
}
