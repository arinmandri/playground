package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.member.Member;

import java.time.Instant;


public interface Y_PostDetail
{
	Long getId ();

	Member getAuthor ();

	String getContent ();

	Instant getCreatedAt ();

	// TODO att
}
