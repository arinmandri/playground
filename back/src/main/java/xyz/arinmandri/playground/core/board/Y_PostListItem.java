package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.VPagable;
import xyz.arinmandri.playground.core.member.Member;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


public interface Y_PostListItem extends VPagable<Long>
{
	Long getId ();

	Member getAuthor ();

	String getContent ();

	Instant getCreatedAt ();

	@JsonIgnore
	List<Y_PAttachmentImage> getAttachmentsImage ();

	@JsonIgnore
	List<Y_PAttachmentFile> getAttachmentsFile ();

	default List<Y_PAttachment> getAttachments () {
		return PostSer.collectAttachments(getAttachmentsImage(), getAttachmentsFile() );
	}

	@Override
	default Long cursor () {
		return getId();
	}
}
