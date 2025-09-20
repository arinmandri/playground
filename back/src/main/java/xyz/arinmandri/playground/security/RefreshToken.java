package xyz.arinmandri.playground.security;

import xyz.arinmandri.playground.core.member.Member;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table( name = "refresh_token" )
@NoArgsConstructor
@Getter
public class RefreshToken
{

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	@Column( updatable = false )
	private Long id;

	@JoinColumn( name = "belongs_to__m" , nullable = false , updatable = false )
	@ManyToOne
	private Member owner;

	@Column( nullable = false , updatable = false , unique = true )
	private String refreshToken;

	@Column( nullable = false , updatable = false )
	private Instant expiresAt;

	public RefreshToken( Member owner , String refreshToken , Instant expiresAt ) {
		super();
		this.owner = owner;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}
}