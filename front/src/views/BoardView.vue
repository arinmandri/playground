<template>
  <div>
    <h1>게시판</h1>
    <table border="1">
      <thead>
        <tr>
          <th>내용</th>
          <th>작성자</th>
          <th>작성일시</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="post in boardList" :key="post.id">
          <td>{{ post.content }}</td>
          <td>{{ post.author.nick }}</td>
          <td>{{ post.createdAt }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { fetchBoardList } from "@/api/board";

// 게시글 데이터 타입 정의
interface Post {
  id: number;
  content: string;
  author: Member;
  createdAt: string;
}

interface Member {
  id: number;
  nick: string;
  email: string;
  createdAt: string;
}

const boardList = ref<Post[]>([]);

onMounted(async () => {
  try {
    const posts = await fetchBoardList();
    boardList.value = posts
  } catch (error) {
    console.error("데이터를 불러오는데 실패했습니다.", error);
  }
});
</script>
