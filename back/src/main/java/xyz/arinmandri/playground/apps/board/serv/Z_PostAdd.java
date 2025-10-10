package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.model.post.PAuthor;
import xyz.arinmandri.playground.apps.board.model.post.Post;

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
