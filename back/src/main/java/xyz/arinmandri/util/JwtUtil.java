package xyz.arinmandri.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


/**
 * jjwt 0.12.6
 * 참고: https://github.com/jwtk/jjwt
 */
public class JwtUtil
{
	private final String issuer;
	private final SecretKey key;

	public JwtUtil( String issuer , String secretKeyStr ) {
		this.issuer = issuer;
		this.key = Keys.hmacShaKeyFor( secretKeyStr.getBytes( StandardCharsets.UTF_8 ) );
	}

	/**
	 * JWT 생성
	 */
	public String generateToken ( String subject , Map<String, ?> claims , long expiresInSec ) {

		Date now = new Date();

		return Jwts.builder()
		        .header()
		        .keyId( "aKeyId" )// XXX
		        .and()
		        .issuer( issuer )
		        .issuedAt( now )
		        .expiration( new Date( now.getTime() + expiresInSec * 1000 ) )
		        .subject( subject )
		        .claims( claims )
		        .signWith( key )
		        .compact();
	}

	/**
	 * 토큰 유효성 검증
	 */
	public boolean validateToken ( String token ) {
		if( token == null ) return false;
		try{
			Jwts.parser()
			        .verifyWith( key )
			        .build()
			        .parseSignedClaims( token );
			return true;
		}
		catch( SignatureException | ExpiredJwtException e ){
			return false;
		}
		catch( Exception e ){
			return false;
		}
	}

	public Claims getClaims ( String token ) {
		try{
			Jws<Claims> what = Jwts.parser()
			        .verifyWith( key )
			        .build()
			        .parseSignedClaims( token );
			return what.getPayload();
		}
		catch( JwtException e ){
			throw e;// TODO exception
		}
	}
}
