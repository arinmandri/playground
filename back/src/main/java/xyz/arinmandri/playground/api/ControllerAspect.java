package xyz.arinmandri.playground.api;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import xyz.arinmandri.playground.security.user.User;


/*
 * 규칙: POST 요청이면 첫 번째 파라미터는 UserDetails이다.
 */
@Aspect
@Component
public class ControllerAspect
{

	@Around( "@annotation(postMapping)" )
	public Object aroundPostMapping ( ProceedingJoinPoint joinPoint , PostMapping postMapping ) throws Throwable {
		Object[] args = joinPoint.getArgs();

		/*
		 * 스프링이 자동 생성한 UserDetails --> 내 커스텀 User
		 */
		User convertedUser = User.from( (UserDetails) args[0] );
		args[0] = convertedUser;

		return joinPoint.proceed( args );
	}
}
