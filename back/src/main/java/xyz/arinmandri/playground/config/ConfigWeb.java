package xyz.arinmandri.playground.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ConfigWeb implements WebMvcConfigurer
{
	/*
	 * CORS 설정.
	 * 개발모드에서는 포트번호가 다름.
	 */
	@Override
	@Profile( "dev" )
	public void addCorsMappings ( CorsRegistry registry ) {
		registry.addMapping( "/**" )
		        .allowedOrigins( "http://localhost:5173" )
		        .allowedMethods( "GET", "POST", "PUT", "DELETE", "OPTIONS" )
		        .allowedHeaders( "*" );
	}
}
