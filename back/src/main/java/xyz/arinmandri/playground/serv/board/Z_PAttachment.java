package xyz.arinmandri.playground.serv.board;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
        @JsonSubTypes.Type( value = Z_PAttachmentNew.class , name = Z_PAttachmentNew.TYPE ),
        @JsonSubTypes.Type( value = Z_PAttachmentOld.class , name = Z_PAttachmentOld.TYPE )
} )
public abstract class Z_PAttachment
{
	public abstract String getType ();
}
