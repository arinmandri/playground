package xyz.arinmandri.playground.file.serv;

public enum FileType
{
	MemberPropic ("/member/propic"),
	PAttachment ("/board/attachment"),
	Temp ("/temp"),
	;

	public final String uploadPath;

	private FileType( String uploadPath ) {
		this.uploadPath = uploadPath;
	}
}
