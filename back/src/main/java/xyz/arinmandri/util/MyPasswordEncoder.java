package xyz.arinmandri.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class MyPasswordEncoder implements PasswordEncoder
{
	final int saltLength = 16;// 바이트 수. 1바이트에 2글자.

	final private SecureRandom random;

	@Override
	public String encode ( CharSequence rawPassword ) {
		String salt = genSalt();
		return hashWithSalt( rawPassword.toString(), salt );
	}

	@Override
	public boolean matches ( CharSequence rawPassword , String encodedPassword ) {
		String salt;
		try{
			salt = encodedPassword.substring( 0, saltLength * 2 );
		}
		catch( StringIndexOutOfBoundsException e ){
			return false;
		}
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private String genSalt () {
		byte[] bytes = new byte[saltLength];
		random.nextBytes( bytes );
		return toHexString( bytes );
	}

	public String toHexString ( byte[] bytes ) {
		StringBuilder sb = new StringBuilder();
		for( byte b : bytes ){
			sb.append( String.format( "%02X", b & 0xFF ) );
		}
		return sb.toString();
	}
}
