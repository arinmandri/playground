package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.a.serv.VPagable;
import xyz.arinmandri.playground.apps.board.model.post.PAuthor;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


public interface Y_PostListItem extends VPagable<Long>
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

	@Override
	default Long cursor () {
		return getId();
	}
}
