package xyz.arinmandri.playground.core.board;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepo extends JpaRepository<Post, Long>
{
	< T > T findById ( Long id , Class<T> type );

	List<Y_PostListItem> findAllByOrderByIdDesc ( Pageable pagable );

	List<Y_PostListItem> findByIdLessThanOrderByIdDesc ( Long beforeId , Pageable pageable );
}
