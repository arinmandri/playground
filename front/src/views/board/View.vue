<template>
  <router-link to="/board/post/write">글쓰기</router-link>
  <div v-if="postListPack.list.length > 0">
    <div class="posts">
      <Y_PostListItemComp v-for="post in postListPack.list" :key="post.id" :post="post"
        @delPost="afterDelPost(post.id)" />
    </div>
    <div class="listEnd">
      <button v-if="!postListPack.isEnd" @click="clickMoreBtn">더보기</button>
    </div>
  </div>
  <div v-else>게시글이 없다.</div>
</template>

<script setup lang="ts">

import Y_PostListItemComp from "./post/comp/PostListItemComp.vue";

import { fetchNextPage, type Y_PostListItem_pretty } from "@/views/board/services/serv"
import type { SimpleListPack } from "@/types/index";

import { ref, onMounted } from "vue";

const postListPack = ref<SimpleListPack<Y_PostListItem_pretty>>({
  list: [],
  cursor: null,
  isEnd: false,
});

// ----------

onMounted(async () => {
  await fetchNextPage(postListPack.value);
});

// ----------

function clickMoreBtn() {
  fetchNextPage(postListPack.value);
}

function afterDelPost(post_id: number) {
  postListPack.value.list = postListPack.value.list.filter(p => p.id !== post_id)
}

</script>

<style scoped>
.posts {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.listEnd {
  text-align: center;
  margin: 20px 0;
}
</style>