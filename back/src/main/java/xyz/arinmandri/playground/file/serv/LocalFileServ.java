package xyz.arinmandri.playground.file.serv;

import xyz.arinmandri.playground.apps.a.serv.exception.ImaDumb;
import xyz.arinmandri.playground.apps.a.serv.exception.Wth;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * 로컬 임시파일 관리.
 * 
 * 임시파일들은 임시파일 폴더 하위에 저장된다.
 * 임시파일 폴더 디렉토리를 30분마다 교대시킨다.
 * 임시파일 폴더 베이스 디렉토리 하위에 버킷 폴더 두 개가 있다.
 * 임시파일은 버킷들 중 하나에 저장되는데 사용할 버킷은 30분마다 교대된다.
 * 교대할 때; 사용하던 버킷은 비우지 않고 그대로 두고, 새로 사용할 버킷은 비우고 시작한다.
 */
@Service
public class LocalFileServ
{
	private static final Logger logger = LoggerFactory.getLogger( LocalFileServ.class );

	private final int tempFileNameLength = 24;

	final private String tempDirBase;
	final private String[] bucketNames;
	private int currentBucketIndex = 0;

	final SecureRandom random;

	public LocalFileServ(
	        @Value( "${local_temp_file.directory-base}" ) String tempDirBase ,
	        @Value( "${local_temp_file.buckets}" ) String[] bucketNames ,
	        @Autowired SecureRandom random ) {

		this.tempDirBase = tempDirBase;
		this.bucketNames = bucketNames;
		this.random = random;

		/*
		 * 앱 시작 시 버킷 폴더가 없으면 생성
		 */
		for( String localBucketName : bucketNames ){
			File folder = new File( tempDirBase + "/" + localBucketName );
			if( !folder.exists() )
			    folder.mkdirs();
		}
	}

	/**
	 * MultipartFile을 임시파일로 저장
	 * 
	 * @param multipartFile null 넣지 말라.
	 * @return 생성된 임시파일
	 */
	public LocalTempFile createTempFile ( MultipartFile multipartFile ) {
		if( multipartFile == null || multipartFile.isEmpty() )
		    throw new ImaDumb();

		String id = generateRandomFileName() + "." + getExtension( multipartFile.getOriginalFilename() );
		final Path path = Paths.get( getTempBucketDir() + "/" + id );

		try{
			multipartFile.transferTo( path );
		}
		catch( IOException | IllegalStateException e ){
			throw new Wth( e );
		}

		return new LocalTempFile( id, path, (int) ( multipartFile.getSize() >> 10 ) );
	}

	/**
	 * MultipartFile 여럿을 임시파일로 저장
	 * 
	 * @return 생성된 임시파일 목록. 입력 순서와 같다.
	 */
	public List<LocalTempFile> createTempFiles ( List<MultipartFile> multipartFiles ) {
		if( multipartFiles == null || multipartFiles.isEmpty() )
		    return List.of();

		return multipartFiles.stream()
		        .map( this::createTempFile )
		        .toList();
	}

	/**
	 * 임시파일 id --> 그 임시파일 찾음
	 *
	 * @param id
	 * @return 그 임시파일.
	 *         없으면 null.
	 */
	public LocalTempFile getTempFile ( String id ) {
		if( id == null || id.isBlank() )
		    return null;

		/*
		 * 현재 버킷 먼저 찾고 없으면 인덱스를 -1씩 하며 차례로 모두 찾는다.
		 */
		final int l = bucketNames.length;
		for( int offset = 0 ; offset < l ; offset++ ){
			int idx = ( currentBucketIndex - offset + l ) % l;
			Path path = Paths.get( getTempBucketDir( idx ) + "/" + id );
			if( path.toFile().exists() ){
				try{
					int size = (int) ( Files.size( path ) >> 10 );
					return new LocalTempFile( id, path, size );
				}
				catch( IOException e ){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public List<LocalTempFile> getTempFiles ( List<String> ids ) {
		return ids.stream().map( this::getTempFile ).toList();
	}

	private String generateRandomFileName () {
		byte[] randomBytes = new byte[tempFileNameLength];
		random.nextBytes( randomBytes );
		return Base64.getUrlEncoder().withoutPadding().encodeToString( randomBytes ).toLowerCase();
	}

	private String getTempBucketDir ( int index ) {
		return tempDirBase + "/" + bucketNames[index];
	}

	private String getTempBucketDir () {
		return getTempBucketDir( currentBucketIndex );
	}

	/**
	 * 파일 이름에서 확장자 추출
	 * 
	 * @param fileName
	 * @return 확장자(소문자), 없으면 빈 문자열
	 */
	public String getExtension ( String fileName ) {

		int li = fileName.lastIndexOf( "." );
		if( li < 0 || li >= fileName.length() - 1 ){
			return "";
		}
		return fileName.substring( li + 1, fileName.length() ).toLowerCase();
	}

	/**
	 * 버킷 교대
	 */
	@Scheduled( cron = "0 */30 * * * *" )
	public void rotateBucket () {
		int newIndex = ( currentBucketIndex + 1 ) % bucketNames.length;
		File folder = new File( getTempBucketDir( newIndex ) );
		// clearFolderRecursive( folder );// LOCAL TEST
		currentBucketIndex = newIndex;
	}

	private void clearFolderRecursive ( File folder ) {
		File[] contents = folder.listFiles();
		if( contents != null ){
			for( File f : contents ){
				if( f.isDirectory() ){
					clearFolderRecursive( f );
					f.delete();
				}
				else
				    f.delete();
			}
		}
	}
}
