package xyz.arinmandri.playground.api;

import xyz.arinmandri.playground.core.member.MKeyBasicRepo;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.security.LackAuthExcp;
import xyz.arinmandri.playground.security.user.UserNormal;

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
 * XXX API 파라미터 유효성 검사 안 하고 있다.
 */
public abstract class ApiA
{
	@Autowired
	MKeyBasicRepo mkeyBasicRepo;

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
	protected static class ExceptionalTask extends RuntimeException
	{
		private static final long serialVersionUID = 1_000_000L;

		final ExcpType type;
		final String msg;

		public ExceptionalTask( ExcpType type , String msg , Throwable cause ) {
			super( cause );
			this.type = type;
			this.msg = msg;
		}

		public ExceptionalTask( ExcpType type , Throwable cause ) {
			super( cause );
			this.type = type;
			this.msg = cause.getMessage();
		}
	}

	/**
	 * 예외 종류.
	 * 현재는 그냥 종류에 따라 똑같은 메시지를 응답할 뿐이지만. 1차원적인 종류 하나에 따라 과연 모든 게 결정될까 싶기도 하다.
	 */
	protected enum ExcpType
	{
		BlameMySelf (HttpStatus.INTERNAL_SERVER_ERROR),
		NoSuchEntity (HttpStatus.NOT_FOUND),
		EntityDuplicate (HttpStatus.CONFLICT),
		LackOfAuth (HttpStatus.FORBIDDEN),
		;

		final HttpStatus status;

		private ExcpType( HttpStatus status ) {
			this.status = status;
		}
	}

}
