package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.Post;

import java.util.List;


public record Z_PostEdit (
        String content ,
        List<Z_PAttachmentNew> attachments )
{

	Post toEntity () {
		return Post.builder()
		        .content( content )
		        .build();
	}
}
