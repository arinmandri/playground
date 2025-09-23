<template>
  <div class="post">
    <div>
      <template v-for="attachment in props.post.attachments">
        <Y_PAttachmentComp :attachment="attachment">
        </Y_PAttachmentComp>
      </template>
    </div>
    <p class="content">{{ props.post.content }}</p>
    <hr />
    <div class="author">
      <span>{{ props.post.author.nick }}</span>
    </div>
    <p class="info">
      <span>{{ props.post.createdAtPretty }}</span>
    </p>
  </div>
</template>

<script setup lang="ts">

import Y_PAttachmentComp from "@/views/board/post/comp/Y_PAttachmentComp.vue";

import { type Y_PostListItem } from "@/views/board/post/types";

const props = defineProps<{
  post: Y_PostListItem;
}>();

</script>

<style scoped>

.post {
  border: 1px solid #e0e0e0;
  background: #fafbfc;
  padding: 16px 16px 2px 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.3s;
}

.post:hover {
  box-shadow: 0 1px 20px #0002;
}

.post .content {
  font-size: 1.1rem;
  margin-bottom: 8px;
  color: #333;
}

.post hr {
  border-top: 2px solid #8882;
  margin: 18px 4px;
}

.post .author {
  font-size: 0.95rem;
  color: var(--point-blue);
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
