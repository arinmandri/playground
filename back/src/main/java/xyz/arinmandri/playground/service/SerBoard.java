package xyz.arinmandri.playground.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;


@Service
public class SerBoard
{
	public List<Map<String, Object>> board () {
		return List.of(
		        Map.of( "id", 123, "title", "제목1", "author", "홍길동", "created_at", "2025-07-20" ),
		        Map.of( "id", 456, "title", "웅덩이와 엉덩이", "author", "김동그랑땡", "created_at", "2025-07-21" ),
		        Map.of( "id", 789, "title", "또비의 스프링", "author", "홍길동", "created_at", "2025-07-22" ) );
	}
}
