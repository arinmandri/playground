package xyz.arinmandri.playground.api;

import java.util.List;

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
import xyz.arinmandri.playground.core.file.LocalFileServ;
import xyz.arinmandri.playground.core.file.LocalTempFile;
import xyz.arinmandri.playground.core.file.S3Serv;


@RestController
@RequestMapping( "/file" )
@RequiredArgsConstructor
public class ApiFile
{
	private static final Logger logger = LoggerFactory.getLogger( ApiFile.class );

	final S3Serv s3Serv;
	final LocalFileServ ltfServ;

	@PostMapping( "/add" )
	// TODO @ClearFile
	public ResponseEntity<apiFileAddResBody> apiFileAdd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        MultipartFile file ) {

		LocalTempFile ltf = ltfServ.createTempFile( file );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( new apiFileAddResBody( ltf.id() ) );
	}

	@PostMapping( "/sadd" )
	// TODO @ClearFile
	public ResponseEntity<List<apiFileAddResBody>> apiFileSadd (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        List<MultipartFile> files ){

		List<LocalTempFile> ltfs = ltfServ.createTempFiles( files );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( ltfs.stream()
		                .map( ltf-> new apiFileAddResBody( ltf.id() ) )
		                .toList() );
	}

	public static record apiFileAddResBody(
	        String id )
	{
	}
}
