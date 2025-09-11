package xyz.arinmandri.util;

import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


class MyPasswordEncoderTest
{
	private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+-=`~|\\[]{};:'\",./<>?";

	final private SecureRandom random = new SecureRandom();

	final private MyPasswordEncoder mpe = new MyPasswordEncoder( random );

	@Test
	void test () {
		for( int i = 0 ; i < 100 ; i++ ){
			String str = genRandomStr();
			String enc = mpe.encode( str );
			try{
				assertEquals( mpe.matches( str, enc ), true );
				assertEquals( mpe.matches( str, enc + genRandomStr() ), false );
				assertEquals( mpe.matches( genRandomStr(), enc ), false );
				assertEquals( mpe.matches( str, "" ), false );
			}
			catch( Throwable e ){
				System.out.println( str );
				System.out.println( enc );
				throw e;
			}
		}
	}

	String genRandomStr () {
		int leng = random.nextInt( 1000 ) + 1;
		StringBuilder sb = new StringBuilder( leng );
		for( int i = 0 ; i < leng ; i++ ){
			int index = random.nextInt( CHAR_SET.length() );
			sb.append( CHAR_SET.charAt( index ) );
		}
		return sb.toString();
	}
}
