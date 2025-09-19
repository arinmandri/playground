package xyz.arinmandri.playground.core;

import java.util.List;

import lombok.Getter;


/**
 * 커서 기반 페이징 응답
 * XXX Long id로만 하는 중
 * 
 * @param <E> 프로젝션 타입
 */
@Getter
public class CursorPage< E extends VPagable<Long> >
{
	private final List<E> list;
	private final Long nextCursor;
	private final int size;

	public CursorPage( List<E> list , int size ) {
		super();
		this.list = list;
		this.nextCursor = list.isEmpty() ? null : list.get( list.size() - 1 ).cursor();
		this.size = size;
	}
}
