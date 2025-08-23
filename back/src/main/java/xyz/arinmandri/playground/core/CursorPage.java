package xyz.arinmandri.playground.core;

import java.util.List;

import lombok.Getter;


/**
 * 커서 기반 페이징 응답
 * 
 * @param <E> 엔터티 타입
 */
@Getter
public class CursorPage< E extends BaseEntity >
{
	private List<E> list;
	private Long nextCursor;
	private int size;

	public CursorPage( List<E> list , int size ) {
		super();
		this.list = list;
		this.nextCursor = list.isEmpty() ? null : list.get( list.size() - 1 ).getId();
		this.size = size;
	}
}