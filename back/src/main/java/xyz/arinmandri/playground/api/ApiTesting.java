package xyz.arinmandri.playground.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ApiTesting extends ApiA
{

	@GetMapping( "/test" )
	public ResponseEntity<?> test1 (
	        Authentication auth ) {
		System.out.println( auth );
		return ResponseEntity.ok()
		        .body( auth );
	}

	@GetMapping( "/test2" )
	public ResponseEntity<?> test2 (
	        @AuthenticationPrincipal UserDetails userDetails ) {
		System.out.println( userDetails );
		return ResponseEntity.ok()
		        .body( userDetails );
	}
}
