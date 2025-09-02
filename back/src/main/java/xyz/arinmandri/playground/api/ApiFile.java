package xyz.arinmandri.playground.api;

import java.io.File;

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
import xyz.arinmandri.playground.core.file.FileSer;


@RestController
@RequestMapping( "/file" )
@RequiredArgsConstructor
public class ApiFile
{
	private static final Logger logger = LoggerFactory.getLogger( ApiFile.class );

	final FileSer fileSer;

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

		fileSer.uploadTest( file1 );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( "File upload is not implemented yet" );
	}
}
