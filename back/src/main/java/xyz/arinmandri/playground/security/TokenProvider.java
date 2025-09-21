package xyz.arinmandri.playground.security;

import xyz.arinmandri.playground.core.member.MKeyBasic;
import xyz.arinmandri.playground.core.member.Member;
import xyz.arinmandri.playground.core.member.MemberSer;
import xyz.arinmandri.playground.security.user.User;
import xyz.arinmandri.util.JwtUtil;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


/**
 * JWT 액세스토큰, 그냥 리프레시 토큰 발행
 */
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

	final private MemberSer memSer;
	final private RefreshTokenRepo refreshTokenRepo;

	final private PasswordEncoder pwEncoder;
	final private JwtUtil jwtUtil;
	final private SecureRandom random;

	/**
	 * 비회원 사용자용 액세스토큰
	 * 
	 * @return 액세스토큰
	 */
	public TokenResponse issueAccessTokenForGuest () {

		byte[] bytes = new byte[10];
		random.nextBytes( bytes );
		String guestName = Base64.getUrlEncoder().withoutPadding().encodeToString( bytes );

		String accessToken = generateToken( User.Type.guest, String.valueOf( guestName ), guestAuthority, duration_ag );

		return new TokenResponse( accessToken, null, guestAuthority, duration_ag );
	}

	/**
	 * 회원용 액세스토큰
	 * 
	 * @return 액세스토큰
	 */
	public TokenResponse issueAccessTokenByBasicKey ( String keyname , String password ) throws LackAuthExcp {

		//// 검증
		MKeyBasic u = memSer.getMKeyBasic( keyname );
		if( u == null || !pwEncoder.matches( password, u.getPassword() ) ){
			throw new LackAuthExcp( "Incorrect keyname or password" );
		}

		//// 발급
		String refreshToken = issueRefreshToken( u.getOwner() );
		String accessToken = generateToken( User.Type.normal, String.valueOf( u.getOwner().getId() ), normalAuthority, duration_a );
		return new TokenResponse( accessToken, refreshToken, normalAuthority, duration_a );
	}

	/**
	 * 리프레시토큰 사용
	 * 
	 * @return 액세스토큰
	 */
	@Transactional
	public TokenResponse issueAccessTokenByRefreshToken ( String refreshToken0 ) throws LackAuthExcp {

		RefreshToken refreshTokenE0 = findByRefreshToken( refreshToken0 );
		if( refreshTokenE0 == null || refreshTokenE0.getExpiresAt().isBefore( Instant.now() ) ){// 이미 만료
			throw new LackAuthExcp( "refresh token invalid" );
		}
		Member member = refreshTokenE0.getOwner();

		String accessToken = generateToken( User.Type.normal, String.valueOf( member.getId() ), normalAuthority, duration_a );
		String refreshToken = issueRefreshToken( member );

		refreshTokenRepo.delete( refreshTokenE0 );// 이전 리프레시토큰 삭제

		return new TokenResponse( accessToken, refreshToken, normalAuthority, duration_a );
	}

	private String generateToken ( User.Type userType , String userName , String scope , long expi ) {

		String userId = User.composeUserId( userType, userName );

		return jwtUtil.generateToken(
		        userId,
		        Map.of(
		                CLAIM_USER, userId,
		                CLAIM_SCOPE, scope ),
		        expi );
	}

	public void deleteRefreshToken ( String refreshToken ) {
		// TODO
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
