package xyz.arinmandri.playground.apps.a.api;

import xyz.arinmandri.playground.file.serv.FileType;
import xyz.arinmandri.playground.file.serv.LocalFileServ;
import xyz.arinmandri.playground.file.serv.LocalTempFile;
import xyz.arinmandri.playground.file.serv.S3Serv;
import xyz.arinmandri.playground.security.LackAuthExcp;
import xyz.arinmandri.playground.security.user.UserNormal;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;


/**
 * 컨트롤러 공통 이용 요소들 + 예외응답처리
 * 
 * memo
 * 
 * HTTP 메서드
 * - GET: 조회용
 * - POST: 데이터 삽입/수정/삭제 등
 * - 그 외 메서드 안 씀.
 * 
 */
public abstract class ApiA
{

	@Autowired
	protected LocalFileServ localFileServ;

	@Autowired
	protected S3Serv s3Serv;

	/**
	 * 로그인 회원의 id
	 * 
	 * @param 인증된 사용자
	 * @return id
	 * @throws LackAuthExcp 사용자가 회원이 아님
	 */
	protected Long myIdAsMember ( UserDetails u ) throws LackAuthExcp {
		if( u instanceof UserNormal un ){
			return un.getMemberId();
		}
		throw new LackAuthExcp( "회원이어야 합니다." );
	}

	/**
	 * 컨트롤러에서 정상 시나리오에서 벗어난 응답시 이걸 던진다.
	 * 우습게도 또 그 딱딱한 자료형의 벽에 부딪혀; 핸들러 메서드의 반환꼴은 정상 시나리오의 응답형식으로 고정이고, 다른 꼴의 응답을 반환하려면 예외던지기로 빼돌려야지.
	 */
	@Getter
	public static class ExceptionalTask extends RuntimeException
	{
		private static final long serialVersionUID = 1_000_000L;

		private final HttpStatus httpStatus;// 클라이언트에게 보여줌.

		public ExceptionalTask( HttpStatus httpStatus , String msg , Throwable cause ) {
			super( msg, cause );
			this.httpStatus = httpStatus;
		}

		public ExceptionalTask( HttpStatus httpStatus , String msg ) {
			super( msg );
			this.httpStatus = httpStatus;
		}

		public ExceptionalTask( HttpStatus httpStatus , Throwable cause ) {
			super( cause );
			this.httpStatus = httpStatus;
		}

		// ----- 전형적인 메시지

		public static ExceptionalTask NOT_FOUND () {
			return new ExceptionalTask( HttpStatus.NOT_FOUND, "대상이 없습니다." );
		}

		public static ExceptionalTask UNPROCESSABLE_ENTITY () {
			return new ExceptionalTask( HttpStatus.UNPROCESSABLE_ENTITY, "대상이 없습니다." );
		}

		public static ExceptionalTask UNAUTHORIZED () {
			return new ExceptionalTask( HttpStatus.UNAUTHORIZED, "인증 안 됨." );
		}

		public static ExceptionalTask FORBIDDEN () {
			return new ExceptionalTask( HttpStatus.FORBIDDEN, "권한 없음." );
		}

		public static ExceptionalTask INTERNAL_SERVER_ERROR () {
			return new ExceptionalTask( HttpStatus.INTERNAL_SERVER_ERROR, "아무튼 내 탓인 듯함." );
		}
	}

	/**
	 * 파일 필드가 업로드타입인 경우 해당 로컬임시파일을 가지고 입력용 DTO에 뭔가 한다.
	 * 용례: r = thisMethod( r, (r)->r.getter(), (r,v)->r.with(v) )
	 * 
	 * @param <T>                파일 필드를 가진 입력용 DTO의 타입.
	 * @param reqDTO             파일 필드를 가진 입력용 DTO.
	 * @param fileFieldGetter    입력용 DTO에서 파일 필드를 가져오는 함수.
	 * @param afterUploadWithLtf 파일 필드 값이 업로드타입인 경우 업로드 완료 후 그 URL을 갖고 실행할 함수.
	 * @return afterUlpoad 실행 결과. 리턴값으로 입력값을 대체하려면 afterUploadWithLtf에서 대체할 값을 반환하게 하라.
	 *         파일 필드 값이 업로드타입이 아니면 그냥 입력용 DTO를 그대로 반환.
	 */
	protected < T > T uploadFileField ( T reqDTO , Function<T, String> fileFieldGetter , BiFunction<T, LocalTempFile, T> afterUploadWithLtf ) {

		String fileField = fileFieldGetter.apply( reqDTO );

		if( fileField != null && fileField.startsWith( "!" ) ){
			String ltfId = fileField.substring( 1 );
			LocalTempFile ltf = localFileServ.getTempFile( ltfId );
			reqDTO = afterUploadWithLtf.apply( reqDTO, ltf );
		}
		return reqDTO;
	}

	/**
	 * 파일 필드가 업로드타입인 경우 업로드 후 그 URL을 가지고 입력용 DTO에 어쩌고 한다.
	 * 
	 * @param <T>                파일 필드를 가진 입력용 DTO의 타입.
	 * @param reqDTO             파일 필드를 가진 입력용 DTO.
	 * @param fileFieldGetter    입력용 DTO에서 파일 필드를 가져오는 함수.
	 * @param afterUploadWithUrl URL을 가지고 입력용 DTO에 어쩌고 하기
	 */
	protected < T > void uploadAndSetFileField ( T reqDTO , FileType fileType , Function<T, String> fileFieldGetter , BiConsumer<T, String> afterUploadWithUrl ) {
		uploadFileField(
		        reqDTO,
		        fileFieldGetter,
		        ( r , ltf )-> {
			        String uploadedUrl = s3Serv.upload( fileType, ltf.path() ).toString();
			        afterUploadWithUrl.accept( r, uploadedUrl );
			        return null;
		        } );
	}

	/**
	 * 파일 필드가 업로드타입인 경우 업로드 후 그 URL을 가지고 입력용 DTO에 어쩌고 한다.
	 * 
	 * @param <T>                파일 필드를 가진 입력용 DTO의 타입.
	 * @param reqDTO             파일 필드를 가진 입력용 DTO.
	 * @param fileFieldGetter    입력용 DTO에서 파일 필드를 가져오는 함수.
	 * @param afterUploadWithUrl URL을 가지고 입력용 DTO에 어쩌고 하고 새 DTO를 반환.
	 * @return afterUploadWithUrl 적용 결과.
	 */
	protected < T > T uploadAndCloneWithNewFileField ( T reqDTO , FileType fileType , Function<T, String> fileFieldGetter , BiFunction<T, String, T> afterUploadWithUrl ) {
		return uploadFileField(
		        reqDTO,
		        fileFieldGetter,
		        ( r , ltf )-> {
			        String uploadedUrl = s3Serv.upload( fileType, ltf.path() ).toString();
			        return afterUploadWithUrl.apply( r, uploadedUrl );
		        } );
	}
}
