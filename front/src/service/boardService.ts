
import type { Post, PostRaw } from "@/types/board";
import type { SimpleListPack } from "@/types/index";
import api from "@/api/axiosInstance";

/* XXX
- 아주 많이 가져온 다음엔; 오래된 항목은 삭제하고; 다음페이지 말고 이전페이지 더보기가 있어야?
- 페이지당 항목 수는 백이 아니라 프론트에? 백에선 페이지 크기 필수로 받고?
*/

//// 게시판 데이터 가져오기
export async function fetchPostList(listPack:SimpleListPack<Post>): Promise<void> {
  try {
    const response = await api.get("/post/list", {
      cursor: listPack.cursor,
    });
    const resData = response.data;
    const newPosts = getPostListFromRawList(resData.list as PostRaw[]);
    listPack.list = [...listPack.list, ...newPosts];
    listPack.cursor = resData.nextCursor;
    listPack.isEnd = listPack.cursor == null;
  } catch (error) {
    console.error("다음 페이지 데이터를 불러오는데 실패했습니다.", error);
  }
}

function getPostListFromRawList(rawList:PostRaw[]):Post[] {
  return rawList.map(rawItem=>({
    ...rawItem,
    createdAt: new Date(rawItem.createdAt),
  }));
}