package xyz.arinmandri.playground.apps.a.serv.exception;

public class ImaDumb extends RuntimeException
{
	private static final long serialVersionUID = 1_000_000L;

	public ImaDumb() {}

	public ImaDumb( Throwable e ) {
		super( e );
	}
}
