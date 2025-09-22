package xyz.arinmandri.playground.core.authedmember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthenticatedMemberRepo extends JpaRepository<AuthenticatedMember, Long>
{}
