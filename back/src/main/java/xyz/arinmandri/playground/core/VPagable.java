package xyz.arinmandri.playground.core;

/**
 * 커서로 페이징 되는 조회 엔터티
 * 
 * @param <T> 커서 자료형
 */
public interface VPagable< T >
{
	T cursor ();
}
