package xyz.arinmandri.playground.core;

import java.nio.file.Path;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.arinmandri.playground.core.file.LocalFileSer;
import xyz.arinmandri.playground.core.file.S3Ser;


@Service
@AllArgsConstructor
public class EntityHandler
{

	final LocalFileSer localFileSer;
	final S3Ser s3Ser;

	/**
	 * 파일필드가 업로드타입인 경우 업로드 후 그 URL을 fileField으로 넣어준다.
	 * 용례: r = thisMethod( r, (r)->r.getter(), (r,v)->r.with(v) )
	 */
	public < T > T uploadFileField ( T req , Function<T, String> getter , BiFunction<T, String, T> cloneWith ){

		String fileField = getter.apply( req );

		if( fileField != null && fileField.startsWith( "!" ) ){
			String ltfId = fileField.substring( 1 );
			Path ltfPath = localFileSer.getTempFilePath( ltfId );
			String uploadedUrl = s3Ser.s3Upload( ltfPath ).toString();
			req = cloneWith.apply( req, uploadedUrl );
		}
		return req;
	}
}
