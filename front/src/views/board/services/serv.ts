
import type { SimpleListPack } from "@/types/index";
import type { Y_PostListItem } from "@/api/schemas";
import { apiPostList } from "@/api/operations";

/* XXX
- 아주 많이 가져온 다음엔; 오래된 항목은 삭제하고; 다음페이지 말고 이전페이지 더보기가 있어야?
- 페이지당 항목 수는 백이 아니라 프론트에? 백에선 페이지 크기 필수로 받고?
*/

//// 게시판 데이터 가져오기
export async function fetchNextPage(listPack: SimpleListPack<Y_PostListItem_pretty>): Promise<void> {
  try {
    const resData = await apiPostList( listPack.cursor );
    const newPosts = getPostListFromRawList(resData.list as Y_PostListItem[]);
    listPack.list.push(...newPosts);
    listPack.cursor = resData.nextCursor;
    listPack.isEnd = listPack.cursor == null;
  } catch (error) {
    console.error("다음 페이지 데이터를 불러오는데 실패했습니다.", error);
  }
}

/**
 * post 목록 응답 API 형식 --> pretty
 */
function getPostListFromRawList(rawList: Y_PostListItem[]): Y_PostListItem_pretty[] {
  return rawList.map(rawItem => ({
    ...rawItem,
    createdAt_: new Date(rawItem.createdAt) as Date,
    get createdAtPretty(): string {
      const date = new Date(this.createdAt);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      return `${year}.${month}.${day}.`;
    }
  }));
}

export interface Y_PostListItem_pretty extends Y_PostListItem {
  createdAt_: Date;
  createdAtPretty: string;
}
