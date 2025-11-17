/*
npm run api 자동생성
*/

import api from "@/api/api";
import type { apiWhoamiRes, CursorPageY_PostListItem, PAuthor, ReqBody_apiPostAdd, ReqBody_MemberAddBasic, Y_MemberForMe, Y_MemberForPublic, Y_PAttachment, Y_PostDetail, Y_PostListItem, Z_MemberAdd, Z_MemberEdit, Z_MKeyBasicAdd, Z_PAttachmentAdd, Z_PAttachmentAddFile, Z_PAttachmentAddImage, Z_PAttachmentNew, Z_PAttachmentNoo, Z_PAttachmentOld, Z_PostEdit } from "@/api/schemas";



// [POST] /post/{post-id}/edit
export async function apiPostEdit( post_id: number , data: Z_PostEdit ): Promise<Y_PostDetail> {
  const response = await api.post(`/post/${post_id}/edit`, data);
  return response.data;
}

// [POST] /post/{post-id}/del
export async function apiPostDel( post_id: number ): Promise<void> {
  const response = await api.post(`/post/${post_id}/del`);
  return response.data;
}

// [POST] /post/add
export async function apiPostAdd( data: ReqBody_apiPostAdd ): Promise<Y_PostDetail> {
  const response = await api.post(`/post/add`, data);
  return response.data;
}

// [POST] /member/me/edit
export async function apiMemberEdit( data: Z_MemberEdit ): Promise<Y_MemberForMe> {
  const response = await api.post(`/member/me/edit`, data);
  return response.data;
}

// [POST] /member/add/basic
export async function apiMemberAddBasic( data: ReqBody_MemberAddBasic ): Promise<Y_MemberForMe> {
  const response = await api.post(`/member/add/basic`, data);
  return response.data;
}

// [GET] /post/{id}
export async function apiPostGet( id: number ): Promise<Y_PostDetail> {
  const params = {
  };
  const response = await api.get(`/post/${id}`, params);
  return response.data;
}

// [GET] /post/list
export async function apiPostList( cursor: (number)|null ): Promise<CursorPageY_PostListItem> {
  const params = {
    cursor,
  };
  const response = await api.get(`/post/list`, params);
  return response.data;
}

// [GET] /member/{id}
export async function apiMemberGet( id: number ): Promise<Y_MemberForPublic> {
  const params = {
  };
  const response = await api.get(`/member/${id}`, params);
  return response.data;
}

// [GET] /member/whoami
export async function apiWhoami(  ): Promise<apiWhoamiRes> {
  const params = {
  };
  const response = await api.get(`/member/whoami`, params);
  return response.data;
}

// [GET] /member/me
export async function apiMemberMe(  ): Promise<Y_MemberForMe> {
  const params = {
  };
  const response = await api.get(`/member/me`, params);
  return response.data;
}

