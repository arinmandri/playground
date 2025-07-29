package xyz.arinmandri.playground.core.mkey;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MkeyBasicRepo extends JpaRepository<MkeyBasic, Long>
{
	Optional<MkeyBasic> findByOwnerId ( Long ownerId );
	Optional<MkeyBasic> findByKeyname ( String keyname );
}
