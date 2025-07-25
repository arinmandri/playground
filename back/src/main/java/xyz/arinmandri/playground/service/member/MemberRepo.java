package xyz.arinmandri.playground.service.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepo extends JpaRepository<Member, Long>
{}
