<template>
  <div class="PostListItemComp">
    <div>
      <template v-for="attachment in props.post.attachments">
        <PAttachmentComp :attachment="attachment">
        </PAttachmentComp>
      </template>
    </div>
    <p class="content">{{ props.post.content }}</p>
    <hr />
    <div class="author">
      <span>{{ props.post.author.nick }}</span>
    </div>
    <p class="additional">
      <span v-if="props.post.author.id == myId">
        <router-link :to="'/board/post/write/' + props.post.id">고치기</router-link>
        <button @click="delPost(props.post.id)">지우기</button>
      </span>
      <span class="date">{{ props.post.createdAtPretty }}</span>
    </p>
  </div>
</template>

<script setup lang="ts">

import PAttachmentComp from "@/views/board/post/comp/PAttachmentComp.vue";

import { apiPostDel, type Y_PostListItem_pretty } from "@/api/board";

import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();

import { storeToRefs } from "pinia";

const myId = storeToRefs(authStore).user.value.id;

const props = defineProps<{
  post: Y_PostListItem_pretty;
}>();

const emit = defineEmits<{
  (e: 'delPost'): void;// 이벤트 정의
}>();

// ----------

async function delPost(post_id: number) {
  if (confirm('정말 지우시겠습니까? 복구불가.')) {
    await apiPostDel(post_id);
    emit('delPost');
  }
}

</script>

<style>
.PostListItemComp {
  border: 1px solid #e0e0e0;
  background: #fafbfc;
  padding: 16px 16px 2px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.3s;
}

.PostListItemComp:hover {
  box-shadow: 0 1px 20px #0002;
}

.PostListItemComp .content {
  font-size: 1.1rem;
  margin-bottom: 8px;
  color: #333;
  white-space: pre-wrap;
}

.PostListItemComp hr {
  border-top: 2px solid #8882;
  margin: 18px 4px;
}

.PostListItemComp .author {
  font-size: 0.95rem;
  color: var(--point-blue);
  margin-bottom: 4px;
}

.PostListItemComp .additional {
  text-align: right;
}

.PostListItemComp .additional .date {
  margin-top: 6px;
  font-size: 0.85rem;
  color: #888;
}

.PostListItemComp .attachment-image {
  max-width: 200px;
  max-height: 200px;
}

.PostListItemComp .listEnd {
  text-align: center;
  margin: 20px 0;
}
</style>
