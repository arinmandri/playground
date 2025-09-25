import { FileAndPreview } from '@/types/common';

export interface PAuthor {
  id: number;
  nick: string;
  propic: string | null;
}

export enum ATT_TYPE {
  image = 'image',
  file = 'file',
}

export interface PostWrite {
  content: string;
  attachments: PAttachment[];
}

/**
 * 게시글 첨부물
 */
export class PAttachment {
  private _attType: ATT_TYPE | null;// null: 첨부물 없음
  private _attData: PAttachmentData;

  private constructor(
    attType: ATT_TYPE | null = null,
    attData: PAttachmentData = PAttachmentData.newOne()
  ) {
    this._attType = attType;
    this._attData = attData;
  }

  static newOne(): PAttachment {
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

  setFileIfSettable(tempFileId: string): void {
    if (this._attType == ATT_TYPE.image)
      this._attData.typeImage?.setTempFileId(tempFileId);
    if (this._attType == ATT_TYPE.file)
      this._attData.typeFile?.setTempFileId(tempFileId);
  }

  get attType(): ATT_TYPE | null {
    return this._attType;
  }

  get attData(): PAttachmentData {
    return this._attData;
  }
}

/**
 * 게시글 첨부물 타입별 데이터
 * 한 가지 필드만 값을 가지고 나머지는 undefined이 되도록 관리하라.
 */
export class PAttachmentData {
  private _typeImage: FileAndPreview | undefined;
  private _typeFile: FileAndPreview | undefined;

  private constructor(
    typeImage: FileAndPreview | undefined = undefined,
    typeFile: FileAndPreview | undefined = undefined
  ) {
    this._typeImage = typeImage;
    this._typeFile = typeFile;
  }

  static newOne(): PAttachmentData {
    return new PAttachmentData();
  }

  copy() {
    return new PAttachmentData(
      this._typeImage?.copy(),
      this._typeFile?.copy()
    );
  }

  clear(): void {
    this._typeImage = undefined;
    this._typeFile = undefined;
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

  get typeImage(): FileAndPreview | undefined {
    return this._typeImage;
  }

  get typeFile(): FileAndPreview | undefined {
    return this._typeFile;
  }
}
