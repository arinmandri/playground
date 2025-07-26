package xyz.arinmandri.playground.security;

public class LackAuthExcp extends Exception
{
	private static final long serialVersionUID = 1_000_000L;

	public LackAuthExcp() {}

	public LackAuthExcp( String msg ) {
		super( msg );
	}

}
