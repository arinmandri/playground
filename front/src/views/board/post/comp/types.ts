import type { Y_PAttachment, Y_PAttachmentImage, Y_PAttachmentFile, Z_PAttachmentAdd, Z_PAttachmentAddFile, Z_PAttachmentAddImage, Z_PAttachmentNoo, Z_PAttachmentOld } from '@/api/schemas';// TODO Y_PAttachmentImage, Y_PAttachmentFile
import { FileAndPreview } from '@/types/common';
import { PAttachmentType } from '@/api/schemas';


export enum NOO_TYPE {
  new = 'new',
  old = 'old',
};

export abstract class PAttachmentNooData {
  abstract get type(): NOO_TYPE;
  abstract toZForm(): Z_PAttachmentNoo;
}

export class PAttachmentNewData extends PAttachmentNooData {

  get type(): NOO_TYPE {
    return NOO_TYPE.new;
  }

  content: PAttachmentAddData;

  private constructor(content: PAttachmentAddData) {
    super();
    this.content = content;
  }

  static ofNew(content: PAttachmentAddData): PAttachmentNewData {
    return new PAttachmentNewData(content);
  }

  toZForm(): Z_PAttachmentNoo {
    const a = {
      type: 'new',
      content: this.content.toZForm(),
    };
    return a;
  }
}

export class PAttachmentOldData extends PAttachmentNooData {

  get type(): NOO_TYPE {
    return NOO_TYPE.old;
  }

  originalOrder: number;

  data: Y_PAttachment;

  constructor(originalOrder: number, data: Y_PAttachment) {
    super();
    this.originalOrder = originalOrder;
    this.data = data;
  }

  static fromY(dataRaw: Y_PAttachment): PAttachmentOldData {
    return new PAttachmentOldData(
      dataRaw.order,
      dataRaw,
    );
  }

  toZForm(): Z_PAttachmentOld {
    return {
      type: this.type,
      originalOrder: this.originalOrder,
    }
  }
}

export class PAttachmentAddData {
  private _attType: PAttachmentType | null;// null: 첨부물 없음
  private _attData: PAttachmentData;

  private constructor(
    attType: PAttachmentType | null = null,
    attData: PAttachmentData = PAttachmentData.newOne()
  ) {
    this._attType = attType;
    this._attData = attData;
  }

  static newOne(): PAttachmentAddData {
    return new PAttachmentAddData();
  }

  static ofExisting(type: PAttachmentType, data: any): PAttachmentAddData {
    // XXX type 불안 -_-
    if (type == PAttachmentType.image) {
      return new PAttachmentAddData(type, PAttachmentData.ofExistingImage(data));
    }
    if (type == PAttachmentType.file) {
      return new PAttachmentAddData(type, PAttachmentData.ofExistingFile(data));
    }

    throw new Error(`type: ${type} / but the data is null.`);
  }

  static fromY(dataRaw: Y_PAttachment): PAttachmentAddData {
    const type = dataRaw.type;
    if (type == PAttachmentType.image) {
      const dataImageRaw = dataRaw as Y_PAttachmentImage;
      return PAttachmentAddData.ofExisting(type, FileAndPreview.ofExisting(dataImageRaw.url));
    }
    if (type == PAttachmentType.file) {
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
    this._attType = PAttachmentType.image;
    this._attData.setImage(file);
  }

  setFile(file: File) {
    this._attType = PAttachmentType.file;
    this._attData.setFile(file);
  }

  getFileIfExists(): File | null {
    if (this._attType == PAttachmentType.image)
      return this._attData.typeImage?.file || null;
    if (this._attType == PAttachmentType.file)
      return this._attData.typeFile?.file || null;
    return null;
  }

  setFileIfSettable(tempFileId: string): void {
    if (this._attType == PAttachmentType.image)
      this._attData.typeImage?.setTempFileId(tempFileId);
    if (this._attType == PAttachmentType.file)
      this._attData.typeFile?.setTempFileId(tempFileId);
  }

  toZForm(): Z_PAttachmentAdd {
    if (this.attType == null)
      throw new Error('');// TODO exception

    const content = this.attData.toZForm(this.attType);// as Z_PAttachmentAdd

    return content;
  }

  get attType(): PAttachmentType | null {
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

  getData(type: PAttachmentType): any {

    if (type == PAttachmentType.image)
      return this._typeImage;
    if (type == PAttachmentType.file)
      return this._typeFile;

    return null;
  }

  toZForm(attType: PAttachmentType): Z_PAttachmentAdd {
    if (attType == PAttachmentType.image) {
      const url = this.typeImage?.fieldValue;
      if (url == null) throw new Error();// TODO exception
      return {
        type: PAttachmentType.image.toString(),
        url,
      } as Z_PAttachmentAddImage;
    }
    if (attType == PAttachmentType.file) {
      const url = this.typeFile?.fieldValue;
      if (url == null) throw new Error();// TODO exception
      return {
        type: PAttachmentType.file.toString(),
        url,
      } as Z_PAttachmentAddFile;
    }
    throw new Error('')// TODO exception
  }

  get typeImage(): FileAndPreview | undefined {
    return this._typeImage;
  }

  get typeFile(): FileAndPreview | undefined {
    return this._typeFile;
  }
}
