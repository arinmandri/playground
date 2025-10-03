package xyz.arinmandri.playground.apps.a.serv.exception;

public class Wth extends RuntimeException
{
	public Wth( Exception e ) {
		super( e );
	}

	private static final long serialVersionUID = 1_000_000L;
}
