package xyz.arinmandri.playground.apps.member.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


interface MKeyBasicRepo extends JpaRepository<MKeyBasic, Long>
{
	Optional<MKeyBasic> findByOwnerId ( Long ownerId );

	Optional<MKeyBasic> findByKeyname ( String keyname );
}
