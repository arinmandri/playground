const API_POST_ADD = '/post/add';
const getAPI_POST_EDIT = (post_id: number) => `/post/${post_id}/edit`;
const getAPI_POST_GET = (post_id: number) => `/post/${post_id}`;
const API_POST_LIST = '/post/list';

import type { SimpleListPack } from "@/types/index";
import { type PAuthor, ATT_TYPE } from "@/views/board/services/types";

import api from "@/api/api";

/* XXX
- 아주 많이 가져온 다음엔; 오래된 항목은 삭제하고; 다음페이지 말고 이전페이지 더보기가 있어야?
- 페이지당 항목 수는 백이 아니라 프론트에? 백에선 페이지 크기 필수로 받고?
*/

export async function apiPostGet(post_id: number): Promise<Y_PostDetail> {
  const data = (await api.get(getAPI_POST_GET(post_id))).data as Y_PostDetail;
  return data;
}

//// 게시판 데이터 가져오기
export async function fetchNextPage(listPack: SimpleListPack<Y_PostListItem>): Promise<void> {
  try {
    const response = await api.get(API_POST_LIST, {
      cursor: listPack.cursor,
    });
    const resData = response.data;
    const newPosts = getPostListFromRawList(resData.list as Y_PostListItem_raw[]);
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
function getPostListFromRawList(rawList: Y_PostListItem_raw[]): Y_PostListItem[] {
  return rawList.map(rawItem => ({
    ...rawItem,
    createdAt: new Date(rawItem.createdAt) as Date,
    get createdAtPretty(): string {
      const date = new Date(this.createdAt);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      return `${year}.${month}.${day}.`;
    }
  }));
}


export async function apiPostAdd(data: Z_PostAdd) {
  await api.post(API_POST_ADD, data);
}

export async function apiPostEdit(post_id: number, data: Z_PostEdit) {
  await api.post(getAPI_POST_EDIT(post_id), data);
}

export async function apiPostDel(post_id: number) {
  await api.post(`/post/${post_id}/del`);
}


export interface Y_PostDetail {
  author: PAuthor;
  content: string;
  createdAt: string;
  attachments: Y_PAttachment[];
}

export interface Y_PostListItem_raw {
  id: number;
  content: string;
  author: PAuthor;
  attachments: Y_PAttachment[];
  createdAt: string;
}

export interface Y_PostListItem {
  id: number;
  content: string;
  author: PAuthor;
  attachments: Y_PAttachment[];
  createdAt: Date;
  createdAtPretty: string;
}

export interface Y_PAttachment {
  type: ATT_TYPE;
  id: number;
  order: number;
}

export interface Y_PAttachmentImage extends Y_PAttachment {
  url: string;
}

export interface Y_PAttachmentFile extends Y_PAttachment {
  url: string;
  size: number;
}

export interface Z_PostAdd {
  content: string;
  attachments: Z_PAttachmentNoo[] | null;
}

export interface Z_PostEdit {
  content: string;
  attachments: Z_PAttachmentNoo[] | null;
}

export interface Z_PAttachmentNoo {
  type: string;
}

export interface Z_PAttachmentNew extends Z_PAttachmentNoo {
  content: Z_PAttachmentAdd;
}

export interface Z_PAttachmentOld extends Z_PAttachmentNoo {
  originalOrder: number;
}

export interface Z_PAttachmentAdd {
}

export interface Z_PAttachmentAddImage extends Z_PAttachmentAdd {
  url: string;
}

export interface Z_PAttachmentAddFile extends Z_PAttachmentAdd {
  url: string;
}