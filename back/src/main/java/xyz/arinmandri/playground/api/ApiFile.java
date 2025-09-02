package xyz.arinmandri.playground.api;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import xyz.arinmandri.playground.aws.S3Actions;
import xyz.arinmandri.playground.core.file.FileSer;


@RestController
@RequestMapping( "/file" )
@RequiredArgsConstructor
public class ApiFile
{
	private static final Logger logger = LoggerFactory.getLogger( ApiFile.class );

	final FileSer fileSer;
	static S3Actions s3Actions = new S3Actions();// TODO static 말고 빈으로 바꾸기

	@PostMapping( "/simple/add" )
	public ResponseEntity<String> apiFileSimpleAdd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        MultipartFile file ) {

		// TODO
		File file1 = fileSer.createTempFile( file );
		if( file1 == null ){
			// TODO exception
			return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR )
			        .body( "File upload failed" );
		}

		// TODO
		String bucketName = "arinmandri";
		String key = "testKey";
		String objectPath = file1.getAbsolutePath();
		try{
			CompletableFuture<PutObjectResponse> future = s3Actions.uploadLocalFileAsync( bucketName, key, objectPath );
			future.join();

		}
		catch( RuntimeException rt ){
			Throwable cause = rt.getCause();
			if( cause instanceof S3Exception s3Ex ){
				logger.info( "S3 error occurred: Error message: {}, Error code {}", s3Ex.getMessage(), s3Ex.awsErrorDetails().errorCode() );
			}
			else{
				logger.info( "An unexpected error occurred: " + rt.getMessage() );
			}
			// throw cause;
		}

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( "File upload is not implemented yet" );
	}
}
