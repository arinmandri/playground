package xyz.arinmandri.playground.apps.a.api;

import xyz.arinmandri.playground.apps.a.api.ApiA.ExceptionalTask;
import xyz.arinmandri.playground.security.LackAuthExcp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;

import lombok.AllArgsConstructor;


/**
 * 특정 타입 에러 --> 클라이언트에게 줄 응답 구성
 */
@RestControllerAdvice
@AllArgsConstructor
public class ApiError
{
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( ApiError.class );

	/**
	 * 컨트롤러에서 만든
	 */
	@ExceptionHandler( ExceptionalTask.class )
	public ResponseEntity<String> handleKnownException ( ExceptionalTask e ) {
		return createTextResponse( e.getHttpStatus(), e.getMessage() );
	}

	/**
	 * 서비스예외이지만 처리가 공통
	 */
	@ExceptionHandler( LackAuthExcp.class )
	public ResponseEntity<String> handleKnownException ( LackAuthExcp e ) {
		return createTextResponse( HttpStatus.FORBIDDEN, e.getMessage() );
	}

	//// ---------------------- (◑_◑;;)

	@ExceptionHandler( NullPointerException.class )
	public ResponseEntity<String> handleKnownException ( NullPointerException e ) {
		log.error( "여기 NULL" + exceptionStackTraceToString( e, 10, 1 ) );
		return createTextResponse( HttpStatus.INTERNAL_SERVER_ERROR, "개발자를 탓하세요." );
	}

	//// ---------------------- 스프링 등에서 자동으로 던지는 예외들

	@ExceptionHandler( org.springframework.web.bind.MethodArgumentNotValidException.class )// spring validation
	public ResponseEntity<Map<String, String>> handleValidationExceptions ( org.springframework.web.bind.MethodArgumentNotValidException e ) {

		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach( error-> errors.put( error.getField(), error.getDefaultMessage() ) );

		return ResponseEntity.badRequest().body( errors );
	}

	@ExceptionHandler( org.springframework.core.convert.ConversionFailedException.class )// spring validation
	public ResponseEntity<String> handleValidationExceptions ( org.springframework.core.convert.ConversionFailedException e ) {

		String msg = "요청 파라미터 타입이 올바르지 않습니다.";
		if( e.getValue() != null && e.getTargetType() != null ){
			msg += " [" + e.getValue() + "] 값을 " + getTypeNameMsg( e.getTargetType().getType() ) + " 타입으로 변환할 수 없습니다.";
		}

		return ResponseEntity.badRequest().body( msg );
	}

	@ExceptionHandler( org.springframework.web.servlet.resource.NoResourceFoundException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.web.servlet.resource.NoResourceFoundException e ) {
		return createTextResponse( HttpStatus.NOT_FOUND, "주소를 다시 확인하세요." );
	}

	@ExceptionHandler( org.springframework.web.HttpRequestMethodNotSupportedException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.web.HttpRequestMethodNotSupportedException e ) {
		return createTextResponse( HttpStatus.NOT_FOUND, "HTTP 메서드를 다시 확인하세요." );
	}

	@ExceptionHandler( org.springframework.validation.BindException.class )// DTO 값 설정 실패
	public ResponseEntity<String> handleKnownException ( org.springframework.validation.BindException e ) {
		//// e에 포함된 모든 에러에 대해 메시지를 한 줄씩 출력
		List<ObjectError> ee = e.getAllErrors();
		StringBuilder sb = new StringBuilder();
		for( int i = 0 ; i < ee.size() ; i++ ){
			sb.append( ee.get( i ).getDefaultMessage() );
			if( i != ee.size() - 1 )
			    sb.append( '\n' );
		}
		return createTextResponse( HttpStatus.BAD_REQUEST, sb.toString() );
	}

	//// request body
	@ExceptionHandler( org.springframework.http.converter.HttpMessageNotReadableException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.http.converter.HttpMessageNotReadableException e ) {

		Throwable cause0 = e.getCause();

		if( cause0 == null ){
			return createTextResponse( HttpStatus.BAD_REQUEST, "Request Body는 필수입니다." );
		}

		try{
			throw cause0;
		}
		catch( com.fasterxml.jackson.databind.exc.InvalidFormatException cause ){
			return createTextResponse( HttpStatus.BAD_REQUEST, "요청바디 중 " + getRefPathStr( cause.getPath() ) + "은는 " + getTypeNameMsg( cause.getTargetType() ) + "이어야 합니다." );
		}
		catch( com.fasterxml.jackson.core.JsonParseException cause ){
			return createTextResponse( HttpStatus.BAD_REQUEST, "json 형식이 올바르지 않습니다." );
		}
		catch( com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException cause ){
			return createTextResponse( HttpStatus.BAD_REQUEST, "요청바디 중 " + getRefPathStr( cause.getPath() ) + " 필드는 정의되지 않았습니다." );
		}
		catch( com.fasterxml.jackson.databind.exc.MismatchedInputException cause ){
			return createTextResponse( HttpStatus.BAD_REQUEST, "json 형식이 목적 타입과 맞지 않습니다." );
		}
		catch( com.fasterxml.jackson.databind.JsonMappingException cause ){
			List<Reference> refPath = cause.getPath();
			if( refPath == null || refPath.isEmpty() )
			    return createTextResponse( HttpStatus.BAD_REQUEST, "요청바디가 있지만 처리할 내용이 없습니다.(예: 공백문자로만 이루어진 요청바디)" );

			Throwable e1 = cause.getCause();
			if( e1 == null )
			    return createTextResponse( HttpStatus.BAD_REQUEST, "요청바디 중 " + getRefPathStr( refPath ) + " 필드의 에러: " + cause.getMessage() );

			try{
				throw e1;
			}
			catch( com.fasterxml.jackson.core.JsonParseException e2 ){
				return createTextResponse( HttpStatus.BAD_REQUEST, "요청바디 중 " + getRefPathStr( refPath ) + " 필드의 에러: " + "json 형식이 올바르지 않습니다." );
			}
			catch( Throwable e2 ){}
		}
		catch( Throwable e1 ){}

		return createTextResponse( HttpStatus.BAD_REQUEST, e.getMessage() );
	}

	private String getTypeNameMsg ( Class<?> type ) {
		if( type == null ) return null;

		if( type == String.class ) return "텍스트";
		if( type == Integer.class ) return "정수";
		if( type == Double.class ) return "실수";
		if( type == Float.class ) return "실수";

		return type.getSimpleName();
	}

	private String getRefPathStr ( List<Reference> path ) {
		if( path == null || path.isEmpty() )
		    return "";

		StringBuilder sb = new StringBuilder();

		sb.append( path.get( 0 ).getFieldName() );
		for( int i = 1 ; i < path.size() ; i++ ){
			String field = path.get( i ).getFieldName();
			if( field != null ){
				sb.append( '.' );
				sb.append( path.get( i ).getFieldName() );
			}
			else{
				sb.append( '[' );
				sb.append( path.get( i ).getIndex() );
				sb.append( ']' );
			}
		}

		return sb.toString();
	}

	//// 요청 파라미터 오류 - multipart
	@ExceptionHandler( org.springframework.web.multipart.support.MissingServletRequestPartException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.web.multipart.support.MissingServletRequestPartException e ) {
		return createTextResponse( HttpStatus.BAD_REQUEST,
		        "요청바디 중 " + e.getRequestPartName() + "은는 필수입니다." );
	}

	//// 요청 파라미터 오류
	@ExceptionHandler( org.springframework.web.bind.MissingServletRequestParameterException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.web.bind.MissingServletRequestParameterException e ) {

		String type = e.getParameterType();
		switch( type ){
		case "Integer", "Long" -> type = "정수";
		case "Float", "Double" -> type = "실수";
		case "String" -> type = "텍스트";
		}
		return createTextResponse( HttpStatus.BAD_REQUEST,
		        "요청 파라미터 " + e.getParameterName() + " 은는 필수이며 " + type + " 꼴입니다." );
	}

	@ExceptionHandler( value = MultipartException.class )
	public ResponseEntity<String> handleKnownException ( MultipartException e ) {
		String msg = e.getMessage();
		Throwable cause0 = e.getMostSpecificCause();

		if( cause0 != null ){
			if( cause0.getClass().isAssignableFrom( org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException.class ) ){
				msg = "Content-Type을 \"multipart/form-data\"로 설정하십시오.";
				return createTextResponse( HttpStatus.UNSUPPORTED_MEDIA_TYPE, msg );
			}
		}

		return createTextResponse( HttpStatus.BAD_REQUEST, msg );
	}

	//// 400 BAD_REQUEST
	@ExceptionHandler( value = org.springframework.web.bind.ServletRequestBindingException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.web.bind.ServletRequestBindingException e ) {
		return createTextResponse( HttpStatus.BAD_REQUEST, e.getMessage() );
	}

	@ExceptionHandler( value = org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class )
	public ResponseEntity<String> handleKnownException ( org.springframework.web.method.annotation.MethodArgumentTypeMismatchException e ) {
		Class<?> c = e.getRequiredType();
		return createTextResponse( HttpStatus.BAD_REQUEST, c + e.getMessage() );
	}

	//// ---------------------- unknown exceptions

	@ExceptionHandler( Exception.class )
	public ResponseEntity<String> handleUnknownException ( Exception e ) {
		log.error( "################### ERROR HERE: " + exceptionStackTraceToString( e, -1, -1 ) );
		return createTextResponse( HttpStatus.INTERNAL_SERVER_ERROR, "an unknown error has occurred." );
	}

	//// ---------------------- 기타 메서드

	private ResponseEntity<String> createTextResponse ( HttpStatus httpStatus , String msg ) {
		return ResponseEntity.status( httpStatus )
		        .header( "Content-Type", "text/plain; charset=UTF-8" )
		        .body( msg );
	}

	public static String exceptionStackTraceToString ( Throwable e ) {
		return exceptionStackTraceToString( e, 10, 6 );
	}

	/**
	 * 에러 메시지 및 스택트레이스 출력
	 * 
	 * @param e
	 * @param max1 한 Throwable 단계당 줄수 제한 (음수이면 걍많이)
	 * @param max2 getCause 횟수 제한 (음수이면 걍많이)
	 * @return the stack trace
	 */
	public static String exceptionStackTraceToString ( Throwable e , int max1 , int max2 ) {
		if( max1 < 0 )
		    max1 = 10000;
		if( max2 < 0 )
		    max2 = 10000;

		StringBuilder b = new StringBuilder();

		int count2 = 0;
		while( e != null ){
			b.append( "\n===== ERROR MESSAGE(" )
			        .append( e.getClass().getName() )
			        .append( "): " )
			        .append( e.getMessage() );
			if( ++count2 > max2 ){
				break;
			}
			StackTraceElement[] stackElements = e.getStackTrace();
			int count1 = 0;
			for( StackTraceElement element : stackElements ){
				if( count1++ >= max1 )
				    break;
				b.append( "\n\tAT " )
				        .append( element.toString() );
			}
			if( stackElements.length > count1 ){
				b.append( "\n\t... " )
				        .append( stackElements.length - count1 )
				        .append( " MORE" );
			}
			e = e.getCause();
		}
		if( e != null ){
			b.append( "\n... MORE CAUSES" );
		}

		return b.toString();
	}
}
