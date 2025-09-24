package xyz.arinmandri.playground.serv.board;

import lombok.Getter;


@Getter
public class Z_PAttachmentOld extends Z_PAttachment
{
	public static final String TYPE = "old";

	@Override
	public String getType () {
		return TYPE;
	}

	int originalOrder;// 원래의 order 값. 새 order 값은 배열 내에서의 순서로 정해짐.
}
