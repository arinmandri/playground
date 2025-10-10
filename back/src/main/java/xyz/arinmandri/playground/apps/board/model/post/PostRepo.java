package xyz.arinmandri.playground.apps.board.model.post;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


interface PostRepo extends JpaRepository<Post, Long>
{
	< T > T findById ( Class<T> type , Long id );

	< T > List<T> findAllByOrderByIdDesc ( Class<T> type , Pageable pagable );

	< T > List<T> findByIdLessThanOrderByIdDesc ( Class<T> type , Long beforeId , Pageable pageable );
}
