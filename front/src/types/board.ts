import type { Member } from './member';
import { FileAndPreview } from './common';

export interface PostRaw {
  id: number;
  content: string;
  author: Member;
  createdAt: string;
}
export interface Post {
  id: number;
  content: string;
  author: Member;
  createdAt: Date;
  createdAtPretty: string;
}

/**
 * 게시글 첨부물
 */
export interface PAttachment {
  attType: ATT_TYPE | null;// null: 첨부물 없음
  attData: PAttachmentData;
}
export const getNullAttachment: () => PAttachment = () => ({
  attType: null,
  attData: getNullAttachmentData(),
});

export enum ATT_TYPE {
  image,
  file,
}

/**
 * 게시글 첨부물 타입별 데이터
 * 한 가지 필드만 값을 가지고 나머지는 null이 되도록 관리하라.
 */
export interface PAttachmentData {
  typeImage: FileAndPreview | null;
  typeFile: FileAndPreview | null;
}
export const getNullAttachmentData: () => PAttachmentData = () => ({
  typeImage: null,
  typeFile: null,
});
