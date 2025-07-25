package xyz.arinmandri.playground.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.Getter;
import xyz.arinmandri.playground.service.NoSuchEntity;

public class ApiA
{

	@ExceptionHandler( NoSuchEntity.class )
	public ResponseEntity<ErrorResponse> handleKnownException ( NoSuchEntity e ) {
		return errorResponse( HttpStatus.NOT_FOUND, e );
	}

	ResponseEntity<ErrorResponse> errorResponse ( HttpStatus status , Exception e ) {
		return ResponseEntity.status( status ).body( new ErrorResponse( e ) );
	}

	@Getter
	static class ErrorResponse
	{
		String type;
		String msg;

		ErrorResponse( Exception e ) {
			this.type = e.getClass().getSimpleName();
			this.msg = e.getMessage();
		}
	}
}
