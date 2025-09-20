package xyz.arinmandri.playground.core.board;

import java.util.List;


public record Z_PostEdit (
        String content ,
        List<Z_PAttachmentAdd> attachments )
{

	Post toEntity () {
		return Post.builder()
		        .content( content )
		        .build();
	}
}
