/*
npm run api 자동생성
*/

export interface Z_PAttachmentAdd {
  type: string,
}

export interface Z_PAttachmentAddFile extends Z_PAttachmentAdd {
  url: string,
  size: number,
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
  content: string,
  attachments: (Z_PAttachmentNew | Z_PAttachmentOld)[],
}

export interface PAuthor {
  id: number,
  nick: string,
  propic: string,
}

export interface Y_PAttachment {
  id: number,
  type: string,
  order: number,
}

export interface Y_PostDetail {
  id: number,
  content: string,
  attachments: (Y_PAttachment)[],
  author: PAuthor,
  createdAt: string,
}

export interface ReqBody_apiPostAdd {
  content: string,
  attachments: (Z_PAttachmentNew)[],
}

export interface Z_MemberEdit {
  nick: string,
  email: string,
  propic: string,
}

export interface Y_MemberForMe {
  email: string,
  id: number,
  nick: string,
  propic: string,
}

export interface ReqBody_MemberAddBasic {
  member: Z_MemberAdd,
  key: Z_MKeyBasicAdd,
}

export interface Z_MKeyBasicAdd {
  keyname: string,
  password: string,
}

export interface Z_MemberAdd {
  nick: string,
  email?: string,
  propic?: string,
}

export interface CursorPageY_PostListItem {
  list: (Y_PostListItem)[],
  nextCursor: number,
  size: number,
}

export interface Y_PostListItem {
  id: number,
  content: string,
  attachments: (Y_PAttachment)[],
  author: PAuthor,
  createdAt: string,
}

export interface Y_MemberForPublic {
  id: number,
  nick: string,
  propic: string,
}

export interface apiWhoamiRes {
  type: string,
  id: number,
  nick: string,
  propic: string,
}

