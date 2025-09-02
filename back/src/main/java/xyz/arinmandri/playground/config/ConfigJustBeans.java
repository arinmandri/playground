package xyz.arinmandri.playground.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import xyz.arinmandri.playground.aws.S3Actions;
import xyz.arinmandri.util.JwtUtil;


/*
 * TODO 그냥 순환참조 때문에 덜렁 분리됐을 뿐이다.
 */
@Configuration
public class ConfigJustBeans
{
	@Value( "${jwt.issuer}" )
	private String issuer;
	@Value( "${jwt.secret_key}" )
	private String secret_key;

	@Bean
	public JwtUtil jwtUtil () {
		return new JwtUtil(
		        issuer,
		        secret_key );
	}

	@Bean
	public S3Actions s3Actions () {
		return new S3Actions();
	}

	@Bean
	public SecureRandom random () {
		return new SecureRandom();
	}
}
