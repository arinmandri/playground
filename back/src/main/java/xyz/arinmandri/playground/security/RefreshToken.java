package xyz.arinmandri.playground.security;

import xyz.arinmandri.playground.core.Loginable;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( schema = "playground" )
@NoArgsConstructor
@Getter
public class RefreshToken
{

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( updatable = false )
	private Long id;

	@Column( nullable = false , updatable = false )
	private int ownerType;

	@Column( nullable = false , updatable = false )
	private long ownerId;

	@Column( nullable = false , updatable = false , unique = true )
	private String refreshToken;

	@Column( nullable = false , updatable = false )
	private Instant expiresAt;

	public RefreshToken( Loginable u , String refreshToken , Instant expiresAt ) {
		this.ownerType = u.getLoginableType();
		this.ownerId = u.getId();
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}
}