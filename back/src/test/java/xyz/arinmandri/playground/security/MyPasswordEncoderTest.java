package xyz.arinmandri.playground.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.SecureRandom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MyPasswordEncoderTest
{
	private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=`~|\\[]{};:'\",./<>?";

	@Autowired
	private MyPasswordEncoder mpe;

	@Autowired
	private SecureRandom random;

	@Test
	void test () {
		for( int i = 0 ; i < 100 ; i++ ){
			String str = genRandomStr();
			String enc = mpe.encode( str );
			assertEquals( mpe.matches( str, enc ), true );
			assertEquals( mpe.matches( str, enc + genRandomStr() ), false );
			assertEquals( mpe.matches( genRandomStr(), enc ), false );
			assertEquals( mpe.matches( str, "" ), false );
		}
	}

	String genRandomStr () {
		int leng = random.nextInt() % 1000;
		leng = leng >= 0 ? leng : -leng;
		StringBuilder sb = new StringBuilder( leng );
		for( int i = 0 ; i < leng ; i++ ){
			int index = random.nextInt( CHAR_SET.length() );
			sb.append( CHAR_SET.charAt( index ) );
		}
		return sb.toString();
	}
}
