package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAttachment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
        @JsonSubTypes.Type( value = Z_PAttachmentAddImage.class , name = Z_PAttachmentAddImage.type ),
        @JsonSubTypes.Type( value = Z_PAttachmentAddFile.class , name = Z_PAttachmentAddFile.type )
} )
public abstract class Z_PAttachmentAdd {
	

	public abstract PAttachment toEntity ();
}
