package xyz.arinmandri.playground.core.authedmember;

import xyz.arinmandri.playground.core.BaseEntityWithoutId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table( name="member" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AuthenticatedMember extends BaseEntityWithoutId {

	@Id
	@Column( updatable = false )
	private Long id;
}
