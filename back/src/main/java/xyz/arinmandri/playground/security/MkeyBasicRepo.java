package xyz.arinmandri.playground.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MkeyBasicRepo extends JpaRepository<MkeyBasic, Long>
{
	Optional<MkeyBasic> findByKeyname ( String keyname );
}
