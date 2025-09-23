<template>
  <div class="write-post-view">
    <h1>글쓰기</h1>
    <form @submit.prevent="submitPost">
      <textarea v-model="content" type="text">뭐 쓸라고 했더라</textarea>
      <PAttachmentList ref="attachmentsComp" :title="'첨부파일'" v-model:attachments="(attachments as PAttachment[])" :maxLength="5" />
      <button type="submit" :disabled="loading">라고 쓰기</button>
    </form>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script lang="ts" setup>

import PAttachmentList from '@/views/board/post/comp/PAttachmentList.vue';

import api from "@/api/axiosInstance";
import { PAttachment } from "@/views/board/post/types";

import { ref, type Ref } from 'vue'
import { useRouter } from 'vue-router'; const router = useRouter();

interface PAttachmentList {
  uploadFiles: () => Promise<void>
}

const attachments = ref<PAttachment[]>([]);
const content = ref('')
const attachmentsComp = ref<PAttachmentList>() as Ref<PAttachmentList>;
const loading = ref(false)
const error = ref('')

async function submitPost() {
  error.value = ''
  loading.value = true
  try {
    await attachmentsComp.value.uploadFiles();
    const attsToSend = attachments.value.map(attRaw => attRaw.toApiSendingForm());
    await api.post('/post/add', {
      content: content.value,
      attachments: attsToSend,
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