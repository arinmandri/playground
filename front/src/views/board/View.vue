<template>
  <router-link to="/board/post/write"><button>글쓰기</button></router-link>
  <div v-if="postListPack.list.length > 0">
    <div class="posts">
      <Y_PostListItemComp v-for="post in postListPack.list" :key="post.id" :post="post">
      </Y_PostListItemComp>
    </div>
    <div class="listEnd">
      <button v-if="!postListPack.isEnd" @click="clickMoreBtn">더보기</button>
    </div>
  </div>
  <div v-else>게시글이 없다.</div>
</template>

<script setup lang="ts">

import Y_PostListItemComp from "./post/comp/Y_PostListItemComp.vue";

import type { Y_PostListItem } from "@/views/board/post/types";
import type { SimpleListPack } from "@/types/index";
import { fetchPostList } from "@/views/board/post/service"

import { ref, onMounted } from "vue";

const postListPack = ref<SimpleListPack<Y_PostListItem>>({
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
  gap: 15px;
}

.listEnd {
  text-align: center;
  margin: 20px 0;
}
</style>