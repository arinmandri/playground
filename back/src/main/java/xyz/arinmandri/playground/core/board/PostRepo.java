package xyz.arinmandri.playground.core.board;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepo extends JpaRepository<Post, Long>
{
	public List<Post> findAllByOrderByIdDesc ( Pageable pagable );

	public List<Post> findByIdLessThanOrderByIdDesc ( Long beforeId , Pageable pageable );
}
