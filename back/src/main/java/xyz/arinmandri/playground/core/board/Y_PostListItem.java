package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.VPagable;
import xyz.arinmandri.playground.core.member.Member;

import java.time.Instant;


public interface Y_PostListItem extends VPagable<Long>
{
	Long getId ();

	Member getAuthor ();

	String getContent ();

	Instant getCreatedAt ();

	@Override
	default Long cursor () {
		return getId();
	}
}
