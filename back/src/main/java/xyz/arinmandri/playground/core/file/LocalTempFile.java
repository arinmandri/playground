package xyz.arinmandri.playground.core.file;

import java.nio.file.Path;


/**
 * 로컬 임시파일
 * id: 임시파일 식별자; 확장자를 포함한 파일이름과 같다.
 * path: 임시파일 경로
 * size: 파일 크기 (KB)
 */
public record LocalTempFile( String id , Path path , int size )
{
}
