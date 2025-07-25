package xyz.arinmandri.playground.service;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@EntityListeners( AuditingEntityListener.class )
@MappedSuperclass
@Getter
public abstract class BaseEntity
{
	@Id
	@Column( updatable = false )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;

	@CreatedDate
	@Column( nullable = false , updatable = false )
	private Instant createdAt;

	@Override
	public boolean equals ( Object other ) {
		if( other == null ) return false;
		if( other instanceof BaseEntity ){
			return id == ( (BaseEntity) other ).id;
		}
		return false;
	}

	@Override
	public int hashCode () {
		return (int) (long) id;
	}
}
