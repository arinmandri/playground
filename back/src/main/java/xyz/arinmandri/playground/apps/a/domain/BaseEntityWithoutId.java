package xyz.arinmandri.playground.apps.a.domain;

import jakarta.persistence.MappedSuperclass;

import lombok.Getter;

/**
 * @see BaseEntity
 * 
 * id 생성 전략을 특별히 정의해야 하는 엔터티.
 */
@MappedSuperclass
@Getter
public abstract class BaseEntityWithoutId extends BaseEntity
{
}
