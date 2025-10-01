package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.domain.post.PAttachment;
import xyz.arinmandri.playground.apps.board.domain.post.PAttachmentFile;
import xyz.arinmandri.playground.apps.board.domain.post.PAttachmentImage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
        @JsonSubTypes.Type( value = Z_PAttachmentAddImage.class , name = PAttachmentImage.TYPE ),
        @JsonSubTypes.Type( value = Z_PAttachmentAddFile.class , name = PAttachmentFile.TYPE )
} )
public abstract class Z_PAttachmentAdd
{
	public abstract PAttachment toEntity ();
}
