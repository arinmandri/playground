<template>
  <div class="write-post-view">
    <h1>글쓰기</h1>
    <PostWriteForm @submit="submitPost"></PostWriteForm>
    <p v-if="error" class="error">{{ error }}</p>
  </div>
</template>

<script lang="ts" setup>

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import { apiPostAdd, type Z_PostAdd } from "@/api/board";
import { MsgClass, useMsgStore } from '@/stores/globalMsg'; const msgStore = useMsgStore();

import { ref, type Ref } from 'vue'
import { useRouter } from 'vue-router'; const router = useRouter();

const loading = ref(false)
const error = ref('')

async function submitPost(data: Z_PostAdd) {
  error.value = ''
  loading.value = true
  try {
    await apiPostAdd(data);
    msgStore.addMsg(MsgClass.INFO, '게시했습니다.');
    router.push('/board');
  } catch (e: any) {
    msgStore.addMsg(MsgClass.ERROR, '왠지 실패함.');
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