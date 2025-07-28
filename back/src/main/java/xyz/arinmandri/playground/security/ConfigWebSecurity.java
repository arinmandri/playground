package xyz.arinmandri.playground.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class ConfigWebSecurity
{
	final private JwtFilter jwtFilter;

	@Bean
	public SecurityFilterChain filterChain ( HttpSecurity http ) throws Exception {
		return http
		        .csrf( AbstractHttpConfigurer::disable )
		        .cors( c-> c.configurationSource( corsConfigurationSource() ) )
		        .sessionManagement( session-> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
		        .authorizeHttpRequests( auth-> auth
		                .requestMatchers( "/auth/**" ).permitAll()
		                .anyRequest().authenticated() )
		        .addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class )
		        .build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource () {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins( List.of( "http://localhost:5173" ) );
		configuration.setAllowedMethods( List.of( "GET", "POST", "PUT", "DELETE" ) );
		configuration.setAllowedHeaders( List.of( "*" ) );
		configuration.setAllowCredentials( true );

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration( "/**", configuration );
		return source;
	}
}
