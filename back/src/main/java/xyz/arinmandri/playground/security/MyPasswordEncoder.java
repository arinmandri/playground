package xyz.arinmandri.playground.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class MyPasswordEncoder implements PasswordEncoder
{
	// TODO salt

	@Override
	public String encode ( CharSequence rawPassword ) {
		return hash( rawPassword.toString() );
	}

	@Override
	public boolean matches ( CharSequence rawPassword , String encodedPassword ) {
		String encodedPassword2 = hash( rawPassword.toString() );
		return encodedPassword2.equals( encodedPassword );
	}

	private String hash ( String w ) {
		try{
			MessageDigest md = MessageDigest.getInstance( "SHA3-512" );
			md.update( w.getBytes() );
			return new java.math.BigInteger( md.digest() ).toString( 16 );
		}
		catch( NoSuchAlgorithmException e ){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
