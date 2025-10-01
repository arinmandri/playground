package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.domain.post.PAuthor;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface Y_PostDetail
{
	Long getId ();

	PAuthor getAuthor ();

	String getContent ();

	Instant getCreatedAt ();

	@JsonIgnore
	List<Y_PAttachmentImage> getAttachmentsImage ();

	@JsonIgnore
	List<Y_PAttachmentFile> getAttachmentsFile ();

	default List<Y_PAttachment> getAttachments () {
		return PostServ.collectAttachments( getAttachmentsImage(), getAttachmentsFile() );
	}
}
