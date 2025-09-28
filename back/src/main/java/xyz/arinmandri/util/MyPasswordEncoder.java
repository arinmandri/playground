package xyz.arinmandri.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


/**
 * https://peekabook.tistory.com/entry/spring-security-passwordencoder-with-sha3-and-salt-example
 */
@RequiredArgsConstructor
public class MyPasswordEncoder implements PasswordEncoder
{
	private final int saltLength = 16;// 바이트 수. 1바이트에 2글자.
	private final int encodedResultLength = 128 + saltLength * 2;

	private final SecureRandom random;

	@Override
	public String encode ( CharSequence rawPassword ) {
		String salt = genSalt();
		return hashWithSalt( rawPassword.toString(), salt );
	}

	@Override
	public boolean matches ( CharSequence rawPassword , String encodedPassword ) {

		if( encodedPassword.length() != encodedResultLength )
		    return false;

		String salt = encodedPassword.substring( 0, saltLength * 2 );
		String encodedPassword2 = hashWithSalt( rawPassword.toString(), salt );
		return encodedPassword2.equals( encodedPassword );
	}

	private String hashWithSalt ( String w , String salt ) {
		return salt + hash( salt + w );
	}

	private String hash ( String w ) {
		try{
			MessageDigest md = MessageDigest.getInstance( "SHA3-512" );
			md.update( w.getBytes() );
			return toHexString( md.digest() );
		}
		catch( NoSuchAlgorithmException e ){
			throw new IllegalStateException( "SHA3-512 not supported in this environment 이딴 게 왜 체크예외냐고", e );
		}
	}

	private String genSalt () {
		byte[] bytes = new byte[saltLength];
		random.nextBytes( bytes );
		return toHexString( bytes );
	}

	private String toHexString ( byte[] bytes ) {
		StringBuilder sb = new StringBuilder();
		for( byte b : bytes ){
			sb.append( String.format( "%02X", b & 0xFF ) );
		}
		return sb.toString();
	}
}
