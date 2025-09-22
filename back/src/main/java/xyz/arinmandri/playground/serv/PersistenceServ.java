package xyz.arinmandri.playground.serv;

import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PersistenceServ
{
	protected final int pageSizeDefault = 10;

	protected Pageable defaultPageable = PageRequest.of( 0, pageSizeDefault, Sort.by( "id" ).descending() );

	public void maybeThrowsUniqueViolated ( DataIntegrityViolationException e , String constName , String msg ) throws UniqueViolated {
		if( isByConstraintUnique( e, constName ) )
		    throw new UniqueViolated( e, msg );
	}
	/**
	 * 예외가 유니크 위반 때문에 났는지 확인
	 */
	public boolean isByConstraintUnique ( Throwable e , String constraintName ) {
		Throwable cause = e;
		while( cause != null ){
			if( cause instanceof PSQLException psqlEx ){
				return psqlEx.getSQLState().equals( "23505" )
				        && psqlEx.getMessage().contains( constraintName );
			}
			cause = cause.getCause();
		}
		return false;
	}

	public class UniqueViolated extends ServExcp
	{
		private static final long serialVersionUID = 1_000_000L;

		public UniqueViolated( Throwable cause , String msg ) {
			super( msg, cause );
		}
	}
}
