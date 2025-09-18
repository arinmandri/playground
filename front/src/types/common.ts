

/**
 * id로 페이징하는 목록
 */
export interface SimpleListPack<T> {
  list: T[],
  cursor: number | null,
  isEnd: boolean
}

/**
 * 첨부파일 + 미리보기 + 제출시 사용할 값
 * 
 * 새 항목을 만드는 경우
 * - newFile: 새로 선택한 파일
 * - preview: null --(파일 선택)--> 새로 선택한 파일에서 만들어진 미리보기
 * - fieldValue: null --(파일 업로드 API 호출)--> 첨부파일의 임시파일id
 * 
 * 기존 항목 수정하는 경우, 파일을 새로 선택하지 않는 경우
 * - newFile: null
 * - preview: 기존 항목의 URL
 * - fieldValue: 기존 항목의 URL
 * 
 * 기존 항목 수정하는 경우, 파일을 새로 선택하는 경우
 * - newFile: 새로 선택한 파일
 * - preview: 기존 항목의 URL --(파일 선택)--> 새로 선택한 파일에서 만들어진 미리보기
 * - fieldValue: 기존 항목의 URL --(파일 업로드 API 호출)--> 첨부파일의 임시파일id
 */
export class FileAndPreview {
  private _newFile: File | null;// 첨부 파일
  private _newFile_init: File | null;
  private _preview: string;// 미리보기 (기존 항목을 불러온 경우 기존 항목의 URL, 새 항목을 만들거나 기존 항목을 수정하는 경우 새로 선택한 파일에서 만들어진 미리보기)
  private _preview_init: string;
  private _fieldValue: string | null;// form의 필드에 바인딩할 값 (기존 항목을 불러온 경우 기존 항목의 URL, 새 항목을 만들거나 기존 항목을 수정하는 경우 첨부파일의 임시파일id)
  private _fieldValue_init: string | null;

  private _hasChanged: boolean;

  private constructor(
    newFile: File | null = null,
    preview: string = '',
    fieldValue: string | null = null
  ) {
    this._newFile = newFile;
    this._newFile_init = newFile;
    this._preview = preview;
    this._preview_init = preview;
    this._fieldValue = fieldValue;
    this._fieldValue_init = fieldValue;

    this._hasChanged = false;
  }

  static getNull(): FileAndPreview {
    return new FileAndPreview();
  }

  copy(): FileAndPreview {
    return new FileAndPreview(
      this._newFile,
      this._preview,
      this._fieldValue
    );
  }

  static newOne(): FileAndPreview {
    return new FileAndPreview();
  }

  static ofExisting(currentUrl: string): FileAndPreview {
    return new FileAndPreview(null, currentUrl, currentUrl);
  }

  clear() {
    this._newFile = null;
    this._preview = '';
    this._fieldValue = null;
    this._hasChanged = true;
  }

  reset() {
    this._newFile = this._newFile_init;
    this._preview = this._preview_init;
    this._fieldValue = this._fieldValue_init;
    this._hasChanged = false;
  }

  setFile(newFile: File) {
    this._newFile = newFile;
    this._preview = URL.createObjectURL(newFile);
    this._hasChanged = false;
  }

  setFieldValue(tempFileId: string) {
    this._fieldValue = SERVER_TEMP_FILE_ID_PREFIX + tempFileId;
  }

  get hasNewFile() {
    return this._newFile != null;
  }

  get file() {
    return this._newFile;
  }

  get fileNN(): File {
    if (this._newFile == null)
      throw new Error();// TODO
    return this._newFile;
  }

  get preview() {
    return this._preview;
  }

  get fieldValue() {
    return this._fieldValue;
  }

  get hasChanged() {
    return this._hasChanged;
  }
}

const SERVER_TEMP_FILE_ID_PREFIX = '!';
