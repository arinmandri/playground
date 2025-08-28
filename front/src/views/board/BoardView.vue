<template>
  <div>
    <h1>게시판</h1>
    <div class="posts">
      <div class="post" v-for="post in postListPack.list" :key="post.id">
        <p>{{ post.content }}</p>
        <p>
          <span>{{ post.author.nick }}</span>
          <span>{{ post.createdAt }}</span>
        </p>
      </div>
    </div>
    <div>
      <button v-if="!postListPack.isEnd" @click="clickMoreBtn">더보기</button>
    </div>
  </div>
  <router-link to="/board/post/write">글쓰기</router-link>
</template>

<script setup lang="ts">
import type { Post } from "@/types/board";
import type { SimpleListPack } from "@/types/index";
import { fetchPostList } from "@/service/boardService"

import { ref, onMounted } from "vue";

const postListPack = ref<SimpleListPack<Post>>({
  list: [],
  cursor: null,
  isEnd: false,
});

onMounted(async () => {
  await fetchPostList(postListPack.value);
});

function clickMoreBtn() {
  fetchPostList(postListPack.value);
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