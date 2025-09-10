package xyz.arinmandri.playground.core;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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
		if( other instanceof BaseEntity baseEntity ){
			return id.equals( baseEntity.id );
		}
		return false;
	}

	@Override
	public int hashCode () {
		return (int) (long) id;
	}
	
	public enum ConstraintDesc {
		MkeyBasic_Email ("mkey_basic_keyname_key", "email duplicate."),
		;

		public final String constraintName;
		public final String msg;

		private ConstraintDesc( String constraintName , String msg ) {
			this.constraintName = constraintName;
			this.msg = msg;
		}
		
	}
}
