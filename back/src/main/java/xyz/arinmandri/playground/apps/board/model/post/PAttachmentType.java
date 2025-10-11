package xyz.arinmandri.playground.apps.board.model.post;

public enum PAttachmentType
{
	image,
	file,
	;

	/*
	 * 아 너 무 불 편 한.
	 * enum에 public final 해봤자 상수가 아니라서 엇절수가업는걸.
	 */
	public class Str
	{
		static public final String image = "image";
		static public final String file = "file";
	}
}
