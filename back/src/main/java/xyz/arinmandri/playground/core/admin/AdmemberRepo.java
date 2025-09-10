package xyz.arinmandri.playground.core.admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdmemberRepo extends JpaRepository<Admember, Long>
{
    Optional<Admember> findByKeyname ( String keyname );
}
