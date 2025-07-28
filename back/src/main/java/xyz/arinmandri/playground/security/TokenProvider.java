package xyz.arinmandri.playground.security;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.mkey.MkeyBasic;
import xyz.arinmandri.playground.core.mkey.MkeyBasicRepo;
import xyz.arinmandri.util.JwtUtil;


@Service
@RequiredArgsConstructor
public class TokenProvider
{
	static private final String CLAIM_PREFIX = "arinmandri.xyz/";
	static public final String CLAIM_USER = CLAIM_PREFIX + "user";
	static public final String CLAIM_SCOPE = CLAIM_PREFIX + "scope";
	static public final String guestAuthority = "guest";
	static public final String normalAuthority = "normal";

	@Value( "${jwt.duration_a}" )
	private long duration_a;// 액세스토큰 기한
	@Value( "${jwt.duration_ag}" )
	private long duration_ag;// 비회원 액세스토큰 기한
	@Value( "${jwt.duration_r}" )
	private long duration_r;// 리프레시토큰 기한

	final private MkeyBasicRepo MemberBKRepo;
	final private RefreshTokenRepo refreshTokenRepo;

	final private JwtUtil jwtUtil;
	private SecureRandom random = new SecureRandom();

	public TokenResponse issueAccessTokenForGuest () {

		byte[] bytes = new byte[10];
		random.nextBytes( bytes );
		String guestName = Base64.getUrlEncoder().withoutPadding().encodeToString( bytes );

		String accessToken = generateToken( "guest:" + guestName, normalAuthority, duration_ag );

		return new TokenResponse( accessToken, null, guestAuthority, duration_ag );
	}

	public TokenResponse issueAccessTokenByBasicKey ( String keyname , String password ) throws LackAuthExcp {

		//// 검증
		MkeyBasic u = MemberBKRepo.findByKeyname( keyname ).orElse( null );
		if( u == null || !u.getPassword().equals( password ) ){// TODO 비밀번호 암호화
			throw new LackAuthExcp( "Incorrect keyname or password" );
		}

		//// 발급
		String refreshToken = issueRefreshToken( u.getOwner() );
		String accessToken = generateToken( "member:" + u.getOwner().getId(), normalAuthority, duration_a );
		return new TokenResponse( accessToken, refreshToken, normalAuthority, duration_a );
	}

	@Transactional
	public TokenResponse issueAccessTokenByRefreshToken ( String refreshToken0 ) throws LackAuthExcp {

		RefreshToken refreshTokenE0 = findByRefreshToken( refreshToken0 );
		if( refreshTokenE0 == null || refreshTokenE0.getExpiresAt().isBefore( Instant.now() ) ){// 이미 만료
			throw new LackAuthExcp( "refresh token invalid" );
		}
		Member member = refreshTokenE0.getOwner();

		String accessToken = generateToken( "member:" + member.getId(), normalAuthority, duration_a );
		String refreshToken = issueRefreshToken( member );

		refreshTokenRepo.delete( refreshTokenE0 );// 이전 리프레시토큰 삭제

		return new TokenResponse( accessToken, refreshToken, normalAuthority, duration_a );
	}

	private String generateToken ( String user , String scope , long expi ) {

		return jwtUtil.generateToken(
		        user,
		        Map.of(
		                CLAIM_USER, user,
		                CLAIM_SCOPE, scope ),
		        expi );
	}

	/*
	 * 리프레시토큰 생성
	 */
	private String issueRefreshToken ( Member member ) {

		byte[] bytes = new byte[64];
		random.nextBytes( bytes );
		String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString( bytes );

		RefreshToken refreshTokenE = new RefreshToken(
		        member,
		        refreshToken,
		        Instant.ofEpochMilli( System.currentTimeMillis() + duration_r * 1000 ) );
		refreshTokenRepo.save( refreshTokenE );

		return refreshToken;
	}

	/*
	 * 리프레시토큰 --> 리프레시토큰 엔터티
	 */
	private RefreshToken findByRefreshToken ( String refreshToken ) {
		return refreshTokenRepo.findByRefreshToken( refreshToken ).orElse( null );
	}

	/*
	 * 시간지난 리프레시토큰 삭제
	 */
	@Scheduled( fixedDelay = 1000 * 60 * 60 )
	@Transactional
	public void cleanExpiredRefreshTokens () {
		Instant now = Instant.now();
		int deleted = refreshTokenRepo.deleteAllExpired( now );
	}
}
