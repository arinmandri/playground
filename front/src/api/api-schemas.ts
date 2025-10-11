/*
npm run api 자동생성
*/

export enum PAttachmentType {
  image = 'image',
  file = 'file',
}

export interface apiWhoamiRes {
  id: number,
  nick: string,
  propic: string,
  type: string,
}

export interface CursorPageY_PostListItem {
  list: (Y_PostListItem)[],
  nextCursor: number,
  size: number,
}

export interface PAuthor {
  id: number,
  nick: string,
  propic: string,
}

export interface ReqBody_apiPostAdd {
  attachments: (Z_PAttachmentNew)[],
  content: string,
}

export interface ReqBody_MemberAddBasic {
  key: Z_MKeyBasicAdd,
  member: Z_MemberAdd,
}

export interface Y_MemberForMe {
  email: string,
  id: number,
  nick: string,
  propic: string,
}

export interface Y_MemberForPublic {
  id: number,
  nick: string,
  propic: string,
}

export interface Y_PAttachment {
  id: number,
  order: number,
  type: PAttachmentType,
}

export interface Y_PostDetail {
  attachments: (Y_PAttachment)[],
  author: PAuthor,
  content: string,
  createdAt: string,
  id: number,
}

export interface Y_PostListItem {
  attachments: (Y_PAttachment)[],
  author: PAuthor,
  content: string,
  createdAt: string,
  id: number,
}

export interface Z_MemberAdd {
  email?: string,
  nick: string,
  propic?: string,
}

export interface Z_MemberEdit {
  email: string,
  nick: string,
  propic: string,
}

export interface Z_MKeyBasicAdd {
  keyname: string,
  password: string,
}

export interface Z_PAttachmentAdd {
  type: string,
}

export interface Z_PAttachmentAddFile extends Z_PAttachmentAdd {
  size: number,
  url: string,
}

export interface Z_PAttachmentAddImage extends Z_PAttachmentAdd {
  url: string,
}

export interface Z_PAttachmentNew extends Z_PAttachmentNoo {
  content: Z_PAttachmentAddFile | Z_PAttachmentAddImage,
}

export interface Z_PAttachmentNoo {
  type: string,
}

export interface Z_PAttachmentOld extends Z_PAttachmentNoo {
  originalOrder: number,
}

export interface Z_PostEdit {
  attachments: (Z_PAttachmentNew | Z_PAttachmentOld)[],
  content: string,
}

