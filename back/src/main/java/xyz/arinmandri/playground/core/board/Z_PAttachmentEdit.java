package xyz.arinmandri.playground.core.board;

import xyz.arinmandri.playground.core.board.Z_PAttachmentEdit.EditPostReqAttachmentFile;
import xyz.arinmandri.playground.core.board.Z_PAttachmentEdit.EditPostReqAttachmentImage;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.With;


@JsonTypeInfo( use = JsonTypeInfo.Id.NAME , include = JsonTypeInfo.As.PROPERTY , property = "type" )
@JsonSubTypes( {
        @JsonSubTypes.Type( value = EditPostReqAttachmentImage.class , name = EditPostReqAttachmentImage.type ),
        @JsonSubTypes.Type( value = EditPostReqAttachmentFile.class , name = EditPostReqAttachmentFile.type )
} )
public interface Z_PAttachmentEdit
{
	public String url ();

	public Z_PAttachmentEdit withUrl ( String url );

	public PAttachment toEntity ();

	record EditPostReqAttachmentImage (
	        @With String url ) implements Z_PAttachmentEdit
	{
		public static final String type = "image";// TODO 도메인에서가져와?

		@Override
		public PAttachmentImage toEntity () {
			return PAttachmentImage.builder()
			        .url( url )
			        .build();
		}
	}

	record EditPostReqAttachmentFile (
	        @With String url ) implements Z_PAttachmentEdit
	{
		public static final String type = "file";// TODO 도메인에서가져와?

		@Override
		public PAttachmentFile toEntity () {
			return PAttachmentFile.builder()
			        .url( url )
			        .build();
		}
	}
}
