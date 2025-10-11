
export interface PAuthor {
  id: number;
  nick: string;
  propic: string | null;
}

export enum PAttachmentType {
  image = 'image',
  file = 'file',
}
