package xyz.arinmandri.playground.security;

import xyz.arinmandri.playground.apps.a.api.ApiA;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@RestController
@RequestMapping( "/auth" )
@RequiredArgsConstructor
public class ApiAuth extends ApiA
{
	final private TokenProvider tokenProvider;
	
	@PostMapping( "/token/guest" )
	public ResponseEntity<TokenResponse> apiAuthTokenGuest (
	        @AuthenticationPrincipal UserDetails userDetails ) {
		TokenResponse tokenRes = tokenProvider.issueAccessTokenForGuest();
		return ResponseEntity.ok()
		        .body( tokenRes );
	}

	@PostMapping( "/token/basic" )
	public ResponseEntity<TokenResponse> apiAuthTokenBasic (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody TokenReq req ) {
		TokenResponse tokenRes;
		try{
			tokenRes = tokenProvider.issueAccessTokenByBasicKey( req.keyname, req.password );
		}
		catch( LackAuthExcp e ){
			throw ExceptionalTask.UNAUTHORIZED();
		}
		return ResponseEntity.ok()
		        .body( tokenRes );
	}
	
	@PostMapping("/token/refresh")
	public ResponseEntity<TokenResponse> apiAuthTokenRefresh (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody TokenRefreshReq req ) {
		TokenResponse tokenRes;
		try{
			tokenRes = tokenProvider.issueAccessTokenByRefreshToken( req.refreshToken );
		}
		catch( LackAuthExcp e ){
			throw ExceptionalTask.UNAUTHORIZED();
		}
		return ResponseEntity.ok()
		        .body( tokenRes );
	}

	@PostMapping( "/logout" )
	public ResponseEntity<Void> apiAuthLogout (
	        @AuthenticationPrincipal UserDetails userDetails ,
	        @RequestBody TokenRefreshReq req ) {

		// TODO 주인 맞나 확인
		tokenProvider.deleteRefreshToken( req.refreshToken );

		return ResponseEntity.ok()
		        .body( null );
	}
	@Getter
	@Setter
	static public class TokenReq
	{
		String keyname;
		String password;
	}

	@Getter
	@Setter
	static public class TokenRefreshReq
	{
		String refreshToken;
	}
}
