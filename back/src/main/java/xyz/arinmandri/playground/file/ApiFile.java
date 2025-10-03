package xyz.arinmandri.playground.file;

import xyz.arinmandri.playground.apps.a.api.excption.BlameClient;
import xyz.arinmandri.playground.file.serv.LocalFileServ;
import xyz.arinmandri.playground.file.serv.LocalTempFile;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;


/**
 * 파일 업로드 관련 구현 정리
 * https://peekabook.tistory.com/entry/webapp-api-separate-file-upload-operation
 */
@RestController
@RequestMapping( "/file" )
@RequiredArgsConstructor
public class ApiFile
{
	private static final Logger logger = LoggerFactory.getLogger( ApiFile.class );

	final LocalFileServ ltfServ;

	@PostMapping( "/add" )
	public ResponseEntity<apiFileAddResBody> apiFileAdd (
	        MultipartFile file ) {

		if(file == null || file.isEmpty()) {
			throw new BlameClient( "파일이 없습니다." );
		}

		LocalTempFile ltf = ltfServ.createTempFile( file );

		return ResponseEntity.status( HttpStatus.CREATED )
		        .body( new apiFileAddResBody( ltf.id() ) );
	}

	@PostMapping( "/sadd" )
	public ResponseEntity<List<apiFileAddResBody>> apiFileSadd (
	        List<MultipartFile> files ){

		if( files == null || files.isEmpty() ){
			throw new BlameClient( "파일이 없습니다." );
		}

		int i = 0;
		for( MultipartFile file : files ){
			if( file == null || file.isEmpty() ){
				throw new BlameClient( i + "번째 파일이 없습니다." );
			}
			i += 1;
		}

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
