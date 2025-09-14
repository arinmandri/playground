

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
export interface FileAndPreview {
  newFile: File | null;// 첨부 파일
  preview: string;// 미리보기 (기존 항목을 불러온 경우 기존 항목의 URL, 새 항목을 만들거나 기존 항목을 수정하는 경우 새로 선택한 파일에서 만들어진 미리보기)
  fieldValue: string | null;// form의 필드에 바인딩할 값 (기존 항목을 불러온 경우 기존 항목의 URL, 새 항목을 만들거나 기존 항목을 수정하는 경우 첨부파일의 임시파일id)
}
export const getFileAndPreviewDefaultInitial: () => FileAndPreview = () => ({
  newFile: null,
  preview: '',
  fieldValue: null,
});
