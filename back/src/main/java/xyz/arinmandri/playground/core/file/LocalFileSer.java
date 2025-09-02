package xyz.arinmandri.playground.core.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

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
public class LocalFileSer {
	private static final Logger logger = LoggerFactory.getLogger( LocalFileSer.class );

	private final int tempFileNameLength = 24;

	final private String tempDirBase;
	final private String[] currentBucketNames = { "bucket1", "bucket2" };
	private int currentBucketIndex = 0;

	final SecureRandom random;

	public LocalFileSer(
	        @Value( "${file.temp-directory-base}" ) String tempDirBase ,
	        @Autowired SecureRandom random ) {

		this.tempDirBase = tempDirBase;
		this.random = random;

		/*
		 * 앱 시작 시 버킷 폴더가 없으면 생성
		 */
		for( String localBucketName : currentBucketNames ){
			File folder = new File( tempDirBase + "/" + localBucketName );
			if( !folder.exists() )
			    folder.mkdirs();
		}
	}
    

	/**
	 * MultipartFile을 임시파일로 저장
	 * 
	 * @return 생성된 임시파일
	 *         입력하는 파일이 없으면 null
	 */
	public File createTempFile ( MultipartFile multipartFile ) {
		if( multipartFile == null || multipartFile.isEmpty() )
		    return null;// TODO exception

		final Path path = Paths.get( generateRandomLocalFilePath() + "." + getExtension( multipartFile.getOriginalFilename() ) );

		File file;
		try{
			multipartFile.transferTo( path );
		}
		catch( IOException | IllegalStateException ex ){
			// TODO exception
			return null;
		}
		file = path.toFile();

		return file;
	}

	private String generateRandomLocalFilePath () {
		return getTempBucketDir() + "/" + generateRandomFileName();
	}

	private String generateRandomFileName () {
		byte[] randomBytes = new byte[tempFileNameLength];
		random.nextBytes( randomBytes );
		return Base64.getUrlEncoder().withoutPadding().encodeToString( randomBytes ).toLowerCase();
	}

	private String getTempBucketDir ( int index ) {
		return tempDirBase + "/" + currentBucketNames[index];
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
		int newIndex = ( currentBucketIndex + 1 ) % currentBucketNames.length;
		File folder = new File( getTempBucketDir( newIndex ) );
		clearFolderRecursive( folder );
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
