package xyz.arinmandri.playground.security;

import xyz.arinmandri.util.MyPasswordEncoder;

import java.security.SecureRandom;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;


/**
 * 스프링 시큐리티 설정
 * 
 */
@Configuration
@RequiredArgsConstructor
public class ConfigWebSecurity
{
	final private JwtFilter jwtFilter;
	final private SecureRandom random;

	@Bean
	public SecurityFilterChain filterChain ( HttpSecurity http ) throws Exception {
		return http
		        .csrf( AbstractHttpConfigurer::disable )
		        .cors( c-> c.configurationSource( corsConfigurationSource() ) )
		        .sessionManagement( session-> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) )
		        .authorizeHttpRequests( auth-> auth
		                .requestMatchers( "/auth/**" ).permitAll()
		                .requestMatchers( "/error" ).permitAll()
		                .requestMatchers( "/swagger-*/**", "/v3/api-docs/**" ).permitAll()
		                .requestMatchers( "/favicon.ico" ).permitAll()
		                .anyRequest().authenticated() )
		        .exceptionHandling(ex -> ex
		                .authenticationEntryPoint(
		                        authenticationEntryPoint() )
		            )
		        .addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class )
		        .build();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint () {
		return ( request , response , authException )-> {
			response.sendError( HttpServletResponse.SC_UNAUTHORIZED ); // 401
		};
	}

	// XXX accessDeniedHandler는 뭐 되지도 않음.

	@Bean
	@Primary
	public CorsConfigurationSource corsConfigurationSource () {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins( List.of( "http://localhost:5173", "https://playground.arinmandri.xyz" ) );// XXX 저것도 설정에 넣나
		configuration.setAllowedMethods( List.of( "GET", "POST", "PUT", "DELETE" ) );
		configuration.setAllowedHeaders( List.of( "*" ) );
		configuration.setAllowCredentials( true );

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration( "/**", configuration );
		return source;
	}

	// XXX
	@Bean
	public UserDetailsService userDetailsService (){
		return username-> {
			throw new UsernameNotFoundException( "This application uses JWT only, no local users." );
		};
	}

	// XXX
	@Bean
	public AuthenticationManager authenticationManager (){
		return authentication-> {
			throw new AuthenticationCredentialsNotFoundException( "JWT authentication only" );
		};
	}

	@Bean
	public MyPasswordEncoder myPasswordEncoder () {
		return new MyPasswordEncoder( random );
	}
}
