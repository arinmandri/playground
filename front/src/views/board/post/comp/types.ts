import type { Y_PAttachment, Y_PAttachmentFile, Y_PAttachmentImage, Z_PAttachmentAdd, Z_PAttachmentNew } from '@/api/board';
import { FileAndPreview } from '@/types/common';
import { ATT_TYPE } from '@/views/board/services/types';

export class PAttachmentAddData {
  private _attType: ATT_TYPE | null;// null: 첨부물 없음
  private _attData: PAttachmentData;

  private constructor(
    attType: ATT_TYPE | null = null,
    attData: PAttachmentData = PAttachmentData.newOne()
  ) {
    this._attType = attType;
    this._attData = attData;
  }

  static newOne(): PAttachmentAddData {
    return new PAttachmentAddData();
  }

  static ofExisting(type: ATT_TYPE, data: any): PAttachmentAddData {
    // XXX type 불안 -_-
    if (type == ATT_TYPE.image) {
      return new PAttachmentAddData(type, PAttachmentData.ofExistingImage(data));
    }
    if (type == ATT_TYPE.file) {
      return new PAttachmentAddData(type, PAttachmentData.ofExistingFile(data));
    }

    throw new Error(`type: ${type} / but the data is null.`);
  }

  static fromY(dataRaw: Y_PAttachment): PAttachmentAddData {
    const type = dataRaw.type;
    if (type == ATT_TYPE.image) {
      const dataImageRaw = dataRaw as Y_PAttachmentImage;
      return PAttachmentAddData.ofExisting(type, FileAndPreview.ofExisting(dataImageRaw.url));
    }
    if (type == ATT_TYPE.file) {
      const dataFileRaw = dataRaw as Y_PAttachmentFile;
      return PAttachmentAddData.ofExisting(type, FileAndPreview.ofExisting(dataFileRaw.url));
    }
    throw new Error('convert_Y_PAttachment_to_PAttachment: unknown type');
  }

  copy() {
    return new PAttachmentAddData(
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

  toZForm(): Z_PAttachmentNew | null {
    if (this.attType == null)
      return null;

    const content = this.attData.toZForm(this.attType);// as Z_PAttachmentAdd
    if (content == null) throw new Error('');// TODO exception

    const result = {
      type: 'new',
      content,
    };
    return result;
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
class PAttachmentData {
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

  static ofExistingImage(data: FileAndPreview) {
    return new PAttachmentData(
      data,
      undefined,
    );
  }

  static ofExistingFile(data: FileAndPreview) {
    return new PAttachmentData(
      undefined,
      data,
    );
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

  getData(type: ATT_TYPE): any {

    if (type == ATT_TYPE.image)
      return this._typeImage;
    if (type == ATT_TYPE.file)
      return this._typeFile;

    return null;
  }

  toZForm(attType: ATT_TYPE): Z_PAttachmentAdd | null {
    if (attType == ATT_TYPE.image) {
      const url = this.typeImage?.fieldValue;
      if (url == null) throw new Error();// TODO exception
      return {
        type: ATT_TYPE.image.toString(),
        url,
      }
    }
    if (attType == ATT_TYPE.file) {
      const url = this.typeFile?.fieldValue;
      if (url == null) throw new Error();// TODO exception
      return {
        type: ATT_TYPE.file.toString(),
        url,
      }
    }
    return null;
  }

  get typeImage(): FileAndPreview | undefined {
    return this._typeImage;
  }

  get typeFile(): FileAndPreview | undefined {
    return this._typeFile;
  }
}
