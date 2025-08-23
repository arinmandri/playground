<template>
  <div>
    <h1>게시판</h1>
    <div class="posts">
      <div class="post" v-for="post in boardList" :key="post.id">
        <p>{{ post.content }}</p>
        <p>
          <span>{{ post.author.nick }}</span>
          <span>{{ post.createdAt }}</span>
        </p>
      </div>
    </div>
    <div>
      <button v-if="!isEnd" @click="fetchPostList">더보기</button>
    </div>
  </div>
  <router-link to="/board/post/write">글쓰기</router-link>
</template>

<script setup lang="ts">
import type { Post } from "@/types/board";
import api from "@/api/axiosInstance";

import { ref, onMounted } from "vue";

const boardList = ref<Post[]>([]);
const cursor = ref<number | null>(null);
const isEnd = ref(false);

onMounted(async () => {
  try {
    fetchPostList();
  } catch (error) {
    console.error("데이터를 불러오는데 실패했습니다.", error);
  }
});

//// 게시판 데이터 가져오기
async function fetchPostList() {
  try {
    const response = await api.get("/post/list", {
      cursor: cursor.value,
    });
    const resData = response.data;
    const newPosts = resData.list as Post[];
    boardList.value = [...boardList.value, ...newPosts];
    cursor.value = resData.nextCursor;
    isEnd.value = cursor.value == null;
  } catch (error) {
    console.error("다음 페이지 데이터를 불러오는데 실패했습니다.", error);
  }
}
</script>

<style>
.posts {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.post {
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
}
</style>