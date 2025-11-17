export interface Y_PAttachmentImage extends Y_PAttachment {
  url: string,
}

export interface Y_PAttachmentFile extends Y_PAttachment {
  url: string,
  size: number,
}
