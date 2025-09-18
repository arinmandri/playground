<template>
  <div class="write-post-view">
    <h1>글쓰기</h1>
    <form @submit.prevent="submitPost">
      <textarea v-model="content" type="text" required>뭐 쓸라고 했더라</textarea>
      <InputAttachmentList :title="'첨부파일'" v-model:fileAndPreviews="attachments" :maxLength="5" />
      <button type="submit" :disabled="loading">라고 쓰기</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script lang="ts" setup>

import InputAttachmentList from '@/components/InputAttachmentList.vue';

import api from "@/api/axiosInstance";
import type { FileAndPreview } from "@/types";
import { getFileAndPreviewDefaultInitial } from "@/types";

import { ref } from 'vue'
import { useRouter } from 'vue-router'; const router = useRouter();

const attachments = ref<FileAndPreview[]>([getFileAndPreviewDefaultInitial()]);
const content = ref('')
const loading = ref(false)
const error = ref('')

const submitPost = async () => {
  error.value = ''
  loading.value = true
  try {
    await api.post('/post/add', {
      content: content.value
    });
    content.value = ''
    router.push('/board');
  } catch (e: any) {
    error.value = e?.message || 'Failed to submit post.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.write-post-view {
  max-width: 400px;
  margin: 2rem auto;
}

h1 {
  display: block;
  margin-bottom: 0.5rem;
}

textarea[type="text"] {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
}

button {
  padding: 0.5rem 1rem;
}

.error {
  color: red;
  margin-top: 1rem;
}
</style>