import type { Member } from './member';

export interface Post {
    id: number;
    content: string;
    author: Member;
    createdAt: string;
}
