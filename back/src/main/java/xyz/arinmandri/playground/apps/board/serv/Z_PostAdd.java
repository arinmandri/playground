package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.model.post.PAttachment;
import xyz.arinmandri.playground.apps.board.model.post.PAuthor;
import xyz.arinmandri.playground.apps.board.model.post.Post;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;


public record Z_PostAdd (
        String content ,
        List<@NotNull Z_PAttachmentAdd> attachments
)
{

	Post toEntity ( PAuthor author ) {

		List<PAttachment> atts = null;

		if( attachments != null ){
			atts = new ArrayList<>();

			for( Z_PAttachmentAdd reqAtt : attachments ){
				PAttachment att = reqAtt.toEntity();
				atts.add( att );
			}
		}

		return Post.builder()
		        .author( author )
		        .content( content )
		        .attachments( atts )
		        .build();
	}
}
