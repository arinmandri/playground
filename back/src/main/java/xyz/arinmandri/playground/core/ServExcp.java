package xyz.arinmandri.playground.core;

public abstract class ServExcp extends Exception
{
	private static final long serialVersionUID = 1_000_000L;

	public ServExcp( String msg ) {
		super( msg );
	}

	public ServExcp( String msg , Throwable cause ) {
		super( msg, cause );
	}

	public ServExcp( Throwable cause ) {
		super( cause );
	}
}
