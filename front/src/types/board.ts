import type { Member } from './member';

export interface PostRaw {
  id: number;
  content: string;
  author: Member;
  createdAt: string;
}
export interface Post {
  id: number;
  content: string;
  author: Member;
  createdAt: Date;
  createdAtPretty: string;
}
