package xyz.arinmandri.util;

public class JustUtil
{
	static public void printCauseCause ( Throwable e ) {
		System.out.println( "-----------------" );
		printCauseCause( e, "☆" );
	}

	static private void printCauseCause ( Throwable e , String prefix ) {
		if( e == null ) return;

		System.out.println( prefix + e );
		printCauseCause( e.getCause(), prefix + "ㄴ" );
	}
}
