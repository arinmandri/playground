package xyz.arinmandri.playground.core;

public abstract class SerExcp extends Exception
{
	private static final long serialVersionUID = 1_000_000L;

	public SerExcp( String msg ) {
		super( msg );
	}

	public SerExcp( String msg , Throwable cause ) {
		super( msg, cause );
	}

	public SerExcp( Throwable cause ) {
		super( cause );
	}
}
