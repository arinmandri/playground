package xyz.arinmandri.playground.core;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * @see BaseEntity
 * 
 * 테이블 각각이 독립적으로 id를 생성하면 그만인 엔터티.
 */
@EntityListeners( AuditingEntityListener.class )
@MappedSuperclass
@Getter
public abstract class BaseEntityWithId extends BaseEntity
{
	@Id
	@Column( updatable = false )
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
}
