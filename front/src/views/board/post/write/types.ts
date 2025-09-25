import type { Y_PostDetail } from "@/api/board";
import { PAttachmentAddData } from "@/views/board/post/comp/types";


export class PostWriteData {
  content: string;
  attachments: PAttachmentAddData[];

  private constructor(
    content: string,
    attachments: PAttachmentAddData[]
  ) {
    this.content = content;
    this.attachments = attachments;
  }

  static fromY(dataRaw: Y_PostDetail): PostWriteData {
    const content = dataRaw.content;
    const attachments = dataRaw.attachments.map(PAttachmentAddData.fromY);

    return {
      content,
      attachments,
    }
  }
}
