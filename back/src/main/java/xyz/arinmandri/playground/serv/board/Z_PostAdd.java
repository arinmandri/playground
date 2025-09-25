package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAuthor;
import xyz.arinmandri.playground.core.board.post.Post;

import java.util.List;

import jakarta.validation.constraints.NotNull;


public record Z_PostAdd (
        String content ,
        List<@NotNull Z_PAttachmentNew> attachments )
{

	Post toEntity ( PAuthor author ) {

		return Post.builder()
		        .author( author )
		        .content( content )
		        .build();
	}
}
