package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.member.Member;

import java.time.Instant;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


public interface Y_PostDetail
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
		return PostSer.collectAttachments( getAttachmentsImage(), getAttachmentsFile() );
	}
}
