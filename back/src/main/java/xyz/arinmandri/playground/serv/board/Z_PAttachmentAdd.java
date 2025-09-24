package xyz.arinmandri.playground.serv.board;

import xyz.arinmandri.playground.core.board.post.PAttachment;
import xyz.arinmandri.playground.core.board.post.PAttachmentFile;
import xyz.arinmandri.playground.core.board.post.PAttachmentImage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
                @JsonSubTypes.Type( value = Z_PAttachmentAddImage.class , name = PAttachmentFile.TYPE ),
                @JsonSubTypes.Type( value = Z_PAttachmentAddFile.class , name = PAttachmentImage.TYPE )
} )
public abstract class Z_PAttachmentAdd
{

        public abstract PAttachment toEntity ();
}
