import type { Y_PostDetail } from "@/api/board";
import { PAttachmentAddData, type PAttachmentNooData } from "@/views/board/post/comp/types";


export class PostWriteData {
  content: string;
  attachments: PAttachmentNooData[];

  private constructor(
    content: string,
    attachments: PAttachmentNooData[]
  ) {
    this.content = content;
    this.attachments = attachments;
  }

  static fromY(dataRaw: Y_PostDetail): PostWriteData {
    const content = dataRaw.content;
    // const attachments = dataRaw.attachments.map(PAttachmentAddData.fromY);// TODO

    return {
      content,
      attachments: [],
    }
  }
}
