package xyz.arinmandri.playground.core.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/*
 * 임시파일들은 임시파일 폴더 하위에 저장된다.
 * 임시파일 폴더 디렉토리를 30분마다 교대시킨다.
 * 임시파일 폴더 베이스 디렉토리 하위에 버킷 폴더 두 개가 있다.
 * 임시파일은 버킷들 중 하나에 저장되는데 사용할 버킷은 30분마다 교대된다.
 * 교대할 때; 사용하던 버킷은 비우지 않고 그대로 두고, 새로 사용할 버킷은 비우고 시작한다.
 */
@Service
public class FileSer
{
	private final int tempFileNameLength = 24;

	private final String tempDirBase;
	final private String[] currentBucketNames = { "bucket1", "bucket2" };
	private int currentBucketIndex = 0;

	final SecureRandom random;

	public FileSer(
	        @Value( "${file.temp-directory-base}" ) String tempDirBase ,
	        @Autowired SecureRandom random ) {

		this.tempDirBase = tempDirBase;
		this.random = random;

		/*
		 * 앱 시작 시 버킷 폴더가 없으면 생성
		 */
		for( String bucketName : currentBucketNames ){
			File folder = new File( tempDirBase + "/" + bucketName );
			if( !folder.exists() )
			    folder.mkdirs();
		}
	}

	/**
	 * MultipartFile을 임시파일로 저장
	 */
	public File createTempFile ( MultipartFile multipartFile ) {
		if( multipartFile == null || multipartFile.isEmpty() )
		    return null;

		final Path path = Paths.get( generateRandomLocalFilePath() );

		File file;
		try{
			multipartFile.transferTo( path );
			file = path.toFile();
		}
		catch( Exception e ){
			// TODO exception
			System.out.println( e.getMessage() );
			file = null;
		}

		return file;
	}

	private String generateRandomLocalFilePath () {
		return getTempBucketDir() + "/" + generateRandomFileName();
	}

	private String generateRandomFileName () {
		byte[] randomBytes = new byte[tempFileNameLength];
		random.nextBytes( randomBytes );
		return Base64.getUrlEncoder().withoutPadding().encodeToString( randomBytes );
	}

	private String getTempBucketDir ( int index ) {
		return tempDirBase + "/" + currentBucketNames[index];
	}

	private String getTempBucketDir () {
		return getTempBucketDir( currentBucketIndex );
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
