package xyz.arinmandri.playground.apps.board.serv;

import xyz.arinmandri.playground.apps.board.model.post.PAttachment;
import xyz.arinmandri.playground.apps.board.model.post.PAttachmentType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
        @JsonSubTypes.Type( value = Z_PAttachmentAddImage.class, name = PAttachmentType.Str.image ),
        @JsonSubTypes.Type( value = Z_PAttachmentAddFile.class, name = PAttachmentType.Str.file )
} )
public abstract class Z_PAttachmentAdd
{
	public abstract PAttachment toEntity ();
}
