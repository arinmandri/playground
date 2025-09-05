package xyz.arinmandri.playground.api;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.file.LocalFileSer;
import xyz.arinmandri.playground.core.file.LocalTempFile;
import xyz.arinmandri.playground.core.file.S3Ser;


@RestController
@RequestMapping( "/test" )
@RequiredArgsConstructor
public class ApiTesting extends ApiA
{
	private static final Logger logger = LoggerFactory.getLogger( ApiTesting.class );

	final S3Ser s3Ser;
	final LocalFileSer localFileSer;

	@GetMapping( "/test" )
	public ResponseEntity<?> test1 (
	        Authentication auth ) {
		System.out.println( auth );
		return ResponseEntity.ok()
		        .body( auth );
	}

	@PostMapping( "/fileup" )
	// TODO @ClearFile
	public ResponseEntity<String> apiFileSimpleAdd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        MultipartFile file ){

		LocalTempFile ltf = localFileSer.createTempFile( file );

		URL url = s3Ser.s3Upload( ltf.path() );
		logger.info( "url = {}", url );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( url.toString() );
	}
}
