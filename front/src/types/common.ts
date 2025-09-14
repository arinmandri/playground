

/**
 * id로 페이징하는 목록
 */
export interface SimpleListPack<T> {
  list: T[],
  cursor: number | null,
  isEnd: boolean
}

export interface FileAndPreview {
  newFile: File | null;
  preview: string | null;
}