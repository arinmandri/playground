
import type { Post, PostRaw } from "@/types/board";
import type { SimpleListPack } from "@/types/index";
import api from "@/api/axiosInstance";

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