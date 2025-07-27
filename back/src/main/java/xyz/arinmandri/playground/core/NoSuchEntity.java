package xyz.arinmandri.playground.core;

public class NoSuchEntity extends Exception
{
	private static final long serialVersionUID = 1_000_000L;

	public NoSuchEntity() {
		super( "ENTITY NOT FOUND" );
	}

	public NoSuchEntity( Class<? extends BaseEntity> entClass , long id ) {
		super( entClass.getSimpleName() + " " + id + " not found" );
	}
}
