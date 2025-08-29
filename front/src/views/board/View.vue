<template>
  <div>
    <div class="posts">
      <div class="post" v-for="post in postListPack.list" :key="post.id">
        <p class="content">{{ post.content }}</p>
        <div class="author">
          <span>{{ post.author.nick }}</span>
        </div>
        <p class="info">
          <span>{{ post.createdAtPretty }}</span>
        </p>
      </div>
    </div>
    <div class="listEnd">
      <button v-if="!postListPack.isEnd" @click="clickMoreBtn">더보기</button>
    </div>
  </div>
  <router-link to="/board/post/write"><button class="writeBtn">✎</button></router-link>
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

<style scoped>
.posts {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.post {
  border: 1px solid #e0e0e0;
  background: #fafbfc;
  padding: 16px 16px 2px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.04);
  transition: box-shadow 0.2s;
}
.post:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
}
.post .content {
  font-size: 1.1rem;
  margin-bottom: 8px;
  color: #333;
}
.post .author {
  font-size: 0.95rem;
  color: #4a90e2;
  margin-bottom: 4px;
}
.post .info {
  margin-top: 6px;
  font-size: 0.85rem;
  color: #888;
  text-align: right;
}

.listEnd {
  text-align: center;
  margin: 20px 0;
}

.writeBtn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  background-color: #4a90e2;
  color: white;
  width: 50px;
  height: 50px;
  border-radius: 40%;
  font-size: 1.5rem;
  text-align: center;
  box-shadow: 1px 1px 8px rgba(0,0,0,0.2);
  transition: background-color 0.2s, box-shadow 0.2s;
}

</style>