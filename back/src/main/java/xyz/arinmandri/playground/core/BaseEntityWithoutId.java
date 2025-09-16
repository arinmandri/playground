package xyz.arinmandri.playground.core;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * @see BaseEntity
 * 
 * id 생성 전략을 특별히 정의해야 하는 엔터티.
 */
@EntityListeners( AuditingEntityListener.class )
@MappedSuperclass
@Getter
public abstract class BaseEntityWithoutId extends BaseEntity
{
}
