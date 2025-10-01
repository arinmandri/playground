package xyz.arinmandri.playground.apps.a.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;


/**
 * 대부분 엔터티가 long id, datetime created_at 필드를 가진다.
 * 그래서 공통속성을 모은 추상 부모 클래스.
 * 
 * 아뿔사!! 그런데 id 필드에 @GeneratedValue( strategy = GenerationType.IDENTITY )를 붙이면 오류가 난다!
 * 같은 부모 타입으로 묶여야 하는 여러 타입들이 제각기 테이블을 가지는데
 * 그런데 부모타입도 @Entity 표시를 안 해주면 조인을 못 한다.
 * 그런데 제각기 테이블의 자동생성 id는 부모타입 입장에서 보면 id 충돌이 날 수도 있다며 안 된다는 설명.
 * 몹시 개똥같은 부분이지만 이 부분을 자동으로 뭔가뭔가 해주는 것도 없는 거 같고.
 * 결국 id가 있는 것과 없는 것으로 BaseEntity를 쪼갠다.
 * 충돌하지 않는 id의 공통타입으로 나타내야 하는 엔터티는 id가 없는 쪽을 상속받아서 id 필드를 직접 정의하라.
 */
@EntityListeners( AuditingEntityListener.class )
@MappedSuperclass
@Getter
abstract class BaseEntity
{
	public abstract Long getId ();

	@CreatedDate
	@Column( nullable = false , updatable = false )
	private Instant createdAt;

	@Override
	public boolean equals ( Object other ) {
		if( other == null ) return false;
		if( other instanceof BaseEntity baseEntity ){
			return getId().equals( baseEntity.getId() )
			        && this.getClass() == other.getClass();
		}
		return false;
	}

	@Override
	public int hashCode () {
		return (int) (long) getId();
	}
}
