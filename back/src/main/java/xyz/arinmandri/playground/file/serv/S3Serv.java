package xyz.arinmandri.playground.file.serv;

import xyz.arinmandri.playground.apps.a.serv.exception.Wth;
import xyz.arinmandri.util.S3Actions;

import java.net.URL;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.S3Exception;


@Service
public class S3Serv
{
	private static final Logger logger = LoggerFactory.getLogger( S3Serv.class );

	final private String awsBucketName;
	final private String keyBase;

	final private S3Actions s3Actions;

	public S3Serv(
	        @Value( "${aws.s3.bucket-name}" ) String awsBucketName ,
	        @Value( "${aws.s3.key-base}" ) String keyBase ,
	        @Autowired S3Actions s3Actions ) {

		this.awsBucketName = awsBucketName;
		this.keyBase = keyBase;
		this.s3Actions = s3Actions;
	}

	/**
	 * S3 업로드
	 * 
	 * @param path
	 * @return
	 */
	public URL upload ( FileType type , Path path ) {

		String key = path.getFileName().toString();

		try{
			s3Actions.uploadLocalFile( awsBucketName, keyBase + type.uploadPath + '/' + key, path );
		}
		catch( S3Exception e ){
			logger.info( "S3 error occurred: Error message: {}, Error code {}", e.getMessage(), e.awsErrorDetails().errorCode() );
			throw new Wth( e );
		}
		return getUrl( type, key );
	}

	public URL getUrl ( FileType type , String key ) {

		URL url = s3Actions.utils.getUrl( b-> b
		        .bucket( awsBucketName )
		        .key( keyBase + type.uploadPath + '/' + key ) );

		return url;
	}
}
