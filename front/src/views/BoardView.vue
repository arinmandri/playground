<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";

// 게시글 데이터 타입 정의
interface Post {
  id: number;
  title: string;
  author: string;
  date: string;
}

const boardList = ref<Post[]>([]);

onMounted(async () => {
  try {
    const response = await axios.get<Post[]>("http://localhost:3000/api/board");
    boardList.value = response.data;
  } catch (error) {
    console.error("데이터를 불러오는데 실패했습니다.", error);
  }
});
</script>

<template>
  <div>
    <h1>게시판</h1>
    <table border="1">
      <thead>
        <tr>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="post in boardList" :key="post.id">
          <td>{{ post.title }}</td>
          <td>{{ post.author }}</td>
          <td>{{ post.date }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
