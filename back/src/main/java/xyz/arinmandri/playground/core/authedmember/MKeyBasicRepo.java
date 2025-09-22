package xyz.arinmandri.playground.core.authedmember;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MKeyBasicRepo extends JpaRepository<MKeyBasic, Long>
{
	Optional<MKeyBasic> findByOwnerId ( Long ownerId );

	Optional<MKeyBasic> findByKeyname ( String keyname );
}
