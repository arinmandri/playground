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
export class PAttachment {
  private _attType: ATT_TYPE | null;// null: 첨부물 없음
  private _attData: PAttachmentData;

  private constructor(
    attType: ATT_TYPE | null = null,
    attData: PAttachmentData = PAttachmentData.getNull()
  ) {
    this._attType = attType;
    this._attData = attData;
  }

  static getNull(): PAttachment {
    return new PAttachment();
  }

  copy() {
    return new PAttachment(
      this._attType,
      this._attData.copy()
    );
  }

  clear() {
    this._attType = null;
    this._attData.clear();
  }

  setImage(file: File) {
    this._attType = ATT_TYPE.image;
    this._attData.setImage(file);
  }

  setFile(file: File) {
    this._attType = ATT_TYPE.file;
    this._attData.setFile(file);
  }

  getFileIfExists(): File | null {
    if (this._attType == ATT_TYPE.image)
      return this._attData.typeImage?.file || null;
    if (this._attType == ATT_TYPE.file)
      return this._attData.typeFile?.file || null;
    return null;
  }

  get attType(): ATT_TYPE | null {
    return this._attType;
  }

  get attData(): PAttachmentData {
    return this._attData;
  }
}

export enum ATT_TYPE {
  image,
  file,
}

/**
 * 게시글 첨부물 타입별 데이터
 * 한 가지 필드만 값을 가지고 나머지는 null이 되도록 관리하라.
 */
class PAttachmentData {
  private _typeImage: FileAndPreview | null;
  private _typeFile: FileAndPreview | null;

  private constructor(
    typeImage: FileAndPreview | null = null,
    typeFile: FileAndPreview | null = null
  ) {
    this._typeImage = typeImage;
    this._typeFile = typeFile;
  }

  static getNull(): PAttachmentData {
    return new PAttachmentData();
  }

  copy() {
    return new PAttachmentData(
      this._typeImage?.copy(),
      this._typeFile?.copy()
    );
  }

  clear(): void {
    this._typeImage = null;
    this._typeFile = null;
  }

  setImage(newFile: File): void {
    this.clear();
    this._typeImage = FileAndPreview.newOne();
    this._typeImage.setFile(newFile);
  }

  setFile(newFile: File): void {
    this.clear();
    this._typeFile = FileAndPreview.newOne();
    this._typeFile.setFile(newFile);
  }

  get typeImage(): FileAndPreview | null {
    return this._typeImage;
  }

  get typeFile(): FileAndPreview | null {
    return this._typeFile;
  }
}
