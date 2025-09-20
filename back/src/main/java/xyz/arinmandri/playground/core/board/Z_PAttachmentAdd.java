package xyz.arinmandri.playground.core.board;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
        @JsonSubTypes.Type( value = Z_PAttachmentImageAdd.class, name = Z_PAttachmentImageAdd.type ),
        @JsonSubTypes.Type( value = Z_PAttachmentFileAdd.class, name = Z_PAttachmentFileAdd.type )
} )
public interface Z_PAttachmentAdd
{
	public String url ();

	public Z_PAttachmentAdd withUrl ( String url );

	public PAttachment toEntity ();

}
