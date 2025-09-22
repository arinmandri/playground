package xyz.arinmandri.playground.core.file;

import java.net.URL;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.S3Exception;
import xyz.arinmandri.util.S3Actions;


@Service
public class S3Serv
{
	private static final Logger logger = LoggerFactory.getLogger( S3Serv.class );

	final private String awsBucketName;

	final private S3Actions s3Actions;

	public S3Serv(
	        @Value( "${aws.s3.bucket-name}" ) String awsBucketName ,
	        @Autowired S3Actions s3Actions ) {

		this.awsBucketName = awsBucketName;
		this.s3Actions = s3Actions;
	}

	/**
	 * S3 업로드
	 * 
	 * @param path
	 * @return
	 */
	public URL s3Upload ( Path path ){

		String key = path.getFileName().toString();

		try{
			s3Actions.uploadLocalFile( awsBucketName, key, path );
		}
		catch( S3Exception e ){
			// TODO exception
			logger.info( "S3 error occurred: Error message: {}, Error code {}", e.getMessage(), e.awsErrorDetails().errorCode() );
		}
		catch( RuntimeException e ){
			// TODO exception
			logger.info( "An unexpected error occurred: " + e.getMessage() );
		}
		return getUrl( key );
	}

	public URL getUrl ( String key ) {

		URL url = s3Actions.utils.getUrl( b-> b
		        .bucket( awsBucketName )
		        .key( key ) );

		return url;
	}
}
