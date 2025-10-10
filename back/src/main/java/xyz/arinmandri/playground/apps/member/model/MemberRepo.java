package xyz.arinmandri.playground.apps.member.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
interface MemberRepo extends JpaRepository<Member, Long>
{
	< T > T findById ( Class<T> type , Long id );
}
