package xyz.arinmandri.playground.security;

import java.util.Collection;

import lombok.Getter;


@Getter
public class TokenResponse
{
	private String access_token;
	private String refresh_token;
	private String token_type = "Bearer";
	private String scope;
	private long expires_in;

	public TokenResponse( String access_token , String refresh_token , String scope , long expires_in ) {
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.scope = scope;
		this.expires_in = expires_in;
	}

	public TokenResponse( String access_token , String refresh_token , Collection<String> scopeItems , long expires_in ) {
		this.access_token = access_token;
		this.refresh_token = refresh_token;
		this.scope = String.join( " ", scopeItems );
		this.expires_in = expires_in;
	}
}
