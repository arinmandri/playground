package xyz.arinmandri.playground.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.arinmandri.playground.MyDeepestSecret;
import xyz.arinmandri.util.JwtUtil;
import xyz.arinmandri.util.S3Actions;


@Configuration
public class UtilBeans
{
	@Value( "${jwt.issuer}" )
	private String issuer;
	@Value( "${jwt.secret_key}" )
	private String secret_key;

	@Bean
	public JwtUtil jwtUtil () {
		return new JwtUtil(
		        issuer ,
		        secret_key );
	}

	@Bean
	public S3Actions s3Actions () {
		return S3Actions.getInstance(
		        "ap-northeast-2",
		        MyDeepestSecret.AWS_ACCESS_KEY_ID,
		        MyDeepestSecret.AWS_SECRET_ACCESS_KEY );
	}

	@Bean
	public SecureRandom random () {
		return new SecureRandom();
	}
}
