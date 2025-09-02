package xyz.arinmandri.playground.core.file;

import java.io.File;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.S3Exception;
import xyz.arinmandri.playground.aws.S3Actions;


@Service
public class S3Ser
{
	private static final Logger logger = LoggerFactory.getLogger( S3Ser.class );

	final private String awsBucketName;

	final private S3Actions s3Actions;

	public S3Ser(
	        @Value( "${aws.s3.bucket-name}" ) String awsBucketName ,
	        @Autowired S3Actions s3Actions ) {

		this.awsBucketName = awsBucketName;
		this.s3Actions = s3Actions;
	}

	/**
	 * S3 업로드
	 * 
	 * @param file
	 * @return
	 */
	public URL s3Upload ( File file ) {

		String key = file.getName();
		String objectPath = file.getAbsolutePath();

		try{
			s3Actions.uploadLocalFile( awsBucketName, key, objectPath );
		}
		catch( RuntimeException rt ){
			// TODO exception
			Throwable cause = rt.getCause();
			if( cause instanceof S3Exception s3Ex ){
				logger.info( "S3 error occurred: Error message: {}, Error code {}", s3Ex.getMessage(), s3Ex.awsErrorDetails().errorCode() );
			}
			else{
				logger.info( "An unexpected error occurred: " + rt.getMessage() );
			}
		}
		return getUrl( key );
	}

	public URL getUrl ( String key ) {

		URL url = S3Actions.utils.getUrl( b-> b
		        .bucket( awsBucketName )
		        .key( key ) );

		return url;
	}
}
