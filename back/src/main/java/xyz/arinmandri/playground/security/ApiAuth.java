package xyz.arinmandri.playground.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import xyz.arinmandri.playground.api.ApiA;


@RestController
@RequestMapping( "/auth" )
@RequiredArgsConstructor
public class ApiAuth extends ApiA
{
	final private MKeySer memberKeySer;
	
	@PostMapping( "/token/guest" )
	public ResponseEntity<TokenResponse> apiAuthTokenGuest () {
		TokenResponse tokenRes = memberKeySer.issueAccessTokenForGuest();
		return ResponseEntity.ok()
		        .body( tokenRes );
	}

	@PostMapping( "/token/basic" )
	public ResponseEntity<TokenResponse> apiAuthTokenBasic (
	        @RequestBody TokenReq req ) {
		TokenResponse tokenRes;
		try{
			tokenRes = memberKeySer.issueAccessTokenByBasicKey( req.keyname, req.password );
		}
		catch( LackAuthExcp e ){
			return ResponseEntity.status( HttpStatus.FORBIDDEN )
			        .body( null );
		}
		return ResponseEntity.ok()
		        .body( tokenRes );
	}
	
	@PostMapping("/token/refresh")
	public ResponseEntity<TokenResponse> apiAuthTokenRefresh (
			@RequestBody TokenRefreshReq req) {
		TokenResponse tokenRes;
		try{
			tokenRes = memberKeySer.issueAccessTokenByRefreshToken( req.refreshToken );
		}
		catch( LackAuthExcp e ){
			return ResponseEntity.status( HttpStatus.FORBIDDEN )
			        .body( null );
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
