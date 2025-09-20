package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.member.Member;

import java.util.List;

import jakarta.validation.constraints.NotNull;


public record Z_PostAdd (
        String content ,
        List<@NotNull Z_PAttachmentAdd> attachments )
{

	Post toEntity ( Member author ) {

		return Post.builder()
		        .author( author )
		        .content( content )
		        .build();
	}
}
