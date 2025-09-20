<template>
  <router-link to="/board/post/write"><button>글쓰기</button></router-link>
  <div v-if="postListPack.list.length > 0">
    <div class="posts">
      <div class="post" v-for="post in postListPack.list" :key="post.id">
        <p class="content">{{ post.content }}</p>
        <div>
          <div v-for="attachment in post.attachments">
            <img class="attachment-image" v-if="attachment.type == ATT_TYPE.image"
              :src="(attachment as Y_PAttachmentImage).url" />
            <p v-if="attachment.type == ATT_TYPE.file">
              <a :href="(attachment as Y_PAttachmentFile).url" download>
                파일 {{ (attachment as Y_PAttachmentFile).size }}KB
              </a>
            </p>
          </div>
        </div>
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
  <div v-else>게시글이 없다.</div>
</template>

<script setup lang="ts">
import type { Post, Y_PAttachmentImage, Y_PAttachmentFile } from "@/types/board";
import { ATT_TYPE } from "@/types/board";
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.2s;
}

.post:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
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

.attachment-image {
  max-width: 200px;
  max-height: 200px;
}
.listEnd {
  text-align: center;
  margin: 20px 0;
}
</style>