package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.security.LackAuthExcp;
import xyz.arinmandri.playground.security.TokenProvider;
import xyz.arinmandri.playground.security.TokenResponse;

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
	public ResponseEntity<TokenResponse> apiAuthTokenGuest () {
		TokenResponse tokenRes = tokenProvider.issueAccessTokenForGuest();
		return ResponseEntity.ok()
		        .body( tokenRes );
	}

	@PostMapping( "/token/basic" )
	public ResponseEntity<TokenResponse> apiAuthTokenBasic (
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

	@PostMapping( "/token/admember" )
	public ResponseEntity<TokenResponse> apiAuthTokenAdmember (
	        @RequestBody TokenReq req ) {
		TokenResponse tokenRes;
		try{
			tokenRes = tokenProvider.issueAccessTokenForAdmin( req.keyname, req.password );
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
