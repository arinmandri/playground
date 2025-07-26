package xyz.arinmandri.playground.security;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long>
{
	Optional<RefreshToken> findByRefreshToken ( String refreshToken );

	@Modifying
	@Query( "DELETE FROM RefreshToken t WHERE t.expiresAt < :now" )
	int deleteAllExpired ( @Param( "now" ) Instant now );
}
