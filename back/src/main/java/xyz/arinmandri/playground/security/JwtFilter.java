package xyz.arinmandri.playground.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import xyz.arinmandri.util.JwtUtil;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{
	final private static String HEADER_AUTHORIZATION = "Authorization";
	final private static String TOKEN_PREFIX = "Bearer ";

	final private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal (
	        HttpServletRequest request ,
	        HttpServletResponse response ,
	        FilterChain filterChain ) throws ServletException , IOException {

		String authorizationHeader = request.getHeader( HEADER_AUTHORIZATION );
		String token = ( authorizationHeader != null && authorizationHeader.startsWith( TOKEN_PREFIX ) )
		        ? authorizationHeader.substring( TOKEN_PREFIX.length() )
		        : null;

		if( token != null && jwtUtil.validateToken( token ) ){// 토큰 유효
			Authentication authentication = getAuthentication( token );
			SecurityContextHolder.getContext().setAuthentication( authentication );
		}
		else{
			SecurityContextHolder.clearContext(); // 인증 실패 시 SecurityContext 비우기
		}

		filterChain.doFilter( request, response );
	}

	/*
	 * 토큰 --> 인증 정보
	 */
	private Authentication getAuthentication ( String token ) {
		if( token == null ) return null; // TODO exception

		Claims claims = jwtUtil.getClaims( token );
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(
		        new SimpleGrantedAuthority( claims.get( TokenProvider.CLAIM_SCOPE ).toString() ) );

		return new UsernamePasswordAuthenticationToken(
		        new User( claims.getSubject(), "", authorities ),
		        token,
		        authorities );
	}
}
