package xyz.arinmandri.playground.core.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/*
 * TODO
 * 임시파일을 뭐 처리 끝날 때마다 지우니까 너무 번거로운데 따로 지우지 말고
 * 임시파일 폴더 디렉토리를 30분마다 교대시키고
 * 30분 지난 폴더를 크론잡으로 싹 비우는게 나을 듯
 */
@Service
public class FileSer
{
	private final int tempFileNameLength = 24;

	private final String tempDirBase;

	final SecureRandom random;

	public FileSer(
	        @Value( "${file.temp-directory-base}" ) String tempDirBase ,
	        @Autowired SecureRandom random ) {
		this.tempDirBase = tempDirBase;
		this.random = random;
	}

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

	public String generateRandomLocalFilePath () {
		return tempDirBase + "/" + generateRandomFileName();
	}

	public String generateRandomFileName () {
		byte[] randomBytes = new byte[tempFileNameLength];
		random.nextBytes( randomBytes );
		return Base64.getUrlEncoder().withoutPadding().encodeToString( randomBytes );
	}
}
