import type { Y_PostDetail } from "@/api/board";
import { PAttachmentAdd } from "@/views/board/post/comp/types";


export class PostWrite {
  content: string;
  attachments: PAttachmentAdd[];

  private constructor(
    content: string,
    attachments: PAttachmentAdd[]
  ) {
    this.content = content;
    this.attachments = attachments;
  }

  static fromY(dataRaw: Y_PostDetail): PostWrite {
    const content = dataRaw.content;
    const attachments = dataRaw.attachments.map(PAttachmentAdd.fromY);

    return {
      content,
      attachments,
    }
  }
}
