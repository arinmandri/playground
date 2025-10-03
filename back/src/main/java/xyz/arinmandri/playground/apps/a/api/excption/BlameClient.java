package xyz.arinmandri.playground.apps.a.api.excption;

public class BlameClient extends RuntimeException
{
	private static final long serialVersionUID = 1_000_000L;

	public BlameClient( String msg ) {
		super( msg );
	}
}
