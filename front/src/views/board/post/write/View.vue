<template>
  <div class="write-post-view">
    <h1>글쓰기</h1>
    <PostWriteForm @submit="submitPost"></PostWriteForm>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script lang="ts" setup>

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import { apiPostAdd, type Z_PostAdd } from "@/views/board/services/apicall";

import { ref, type Ref } from 'vue'
import { useRouter } from 'vue-router'; const router = useRouter();

const loading = ref(false)
const error = ref('')

async function submitPost(data: Z_PostAdd) {
  error.value = ''
  loading.value = true
  try {
    await apiPostAdd(data);
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