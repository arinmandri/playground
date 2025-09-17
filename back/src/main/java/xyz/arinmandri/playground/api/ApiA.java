package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.file.LocalFileSer;
import xyz.arinmandri.playground.core.file.LocalTempFile;
import xyz.arinmandri.playground.core.file.S3Ser;
import xyz.arinmandri.playground.core.member.MKeyBasicRepo;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.LackAuthExcp;
import xyz.arinmandri.playground.security.user.UserNormal;

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
	MKeyBasicRepo mkeyBasicRepo;

	@Autowired
	LocalFileSer localFileSer;

	@Autowired
	S3Ser s3Ser;

	/**
	 * UserDetails --> Member
	 */
	protected Member getMemberFrom ( UserDetails u ) {
		if( u instanceof UserNormal un ){
			return mkeyBasicRepo.findByOwnerId( un.getMemberId() )
			        .orElseThrow( ()-> {
				        throw new LackAuthExcp( "없는 회원입니다." );
			        } ).getOwner();
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

		protected ExceptionalTask( HttpStatus httpStatus , String msg , Throwable cause ) {
			super( msg, cause );
			this.httpStatus = httpStatus;
		}

		protected ExceptionalTask( HttpStatus httpStatus , String msg ) {
			super( msg );
			this.httpStatus = httpStatus;
		}

		protected ExceptionalTask( HttpStatus httpStatus , Throwable cause ) {
			super( cause );
			this.httpStatus = httpStatus;
		}

		// ----- 전형적인 메시지

		protected static ExceptionalTask NOT_FOUND () {
			return new ExceptionalTask( HttpStatus.NOT_FOUND, "대상이 없습니다." );
		}

		protected static ExceptionalTask UNPROCESSABLE_ENTITY () {
			return new ExceptionalTask( HttpStatus.UNPROCESSABLE_ENTITY, "대상이 없습니다." );
		}

		protected static ExceptionalTask UNAUTHORIZED () {
			return new ExceptionalTask( HttpStatus.UNAUTHORIZED, "인증 안 됨." );
		}

		protected static ExceptionalTask FORBIDDEN () {
			return new ExceptionalTask( HttpStatus.FORBIDDEN, "권한 없음." );
		}

		protected static ExceptionalTask INTERNAL_SERVER_ERROR () {
			return new ExceptionalTask( HttpStatus.INTERNAL_SERVER_ERROR, "아무튼 내 탓인 듯함." );
		}
	}

	/**
	 * 파일필드가 업로드타입인 경우 업로드 후 그 URL을 fileField으로 넣어준다.
	 * 용례: r = thisMethod( r, (r)->r.getter(), (r,v)->r.with(v) )
	 */
	public < T > T uploadFileField ( T req , Function<T, String> getter , BiFunction<T, String, T> cloneWith ) {

		String fileField = getter.apply( req );

		if( fileField != null && fileField.startsWith( "!" ) ){
			String ltfId = fileField.substring( 1 );
			LocalTempFile ltf = localFileSer.getTempFile( ltfId );
			String uploadedUrl = s3Ser.s3Upload( ltf.path() ).toString();
			req = cloneWith.apply( req, uploadedUrl );
		}
		return req;
	}
}
