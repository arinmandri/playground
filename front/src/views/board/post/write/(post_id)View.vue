<template>
  <div class="write-post-view">
    <h1>고쳐쓰기</h1>
    <PostWriteForm ref="postWriteForm" @submit="submitPost"></PostWriteForm>
  </div>
</template>

<script setup lang="ts">

import { PostWriteData } from './types';

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import { apiPostEdit, type Z_PostAdd } from "@/api/board";

import { ref, type Ref, onMounted } from "vue";
import { useRouter, useRoute } from 'vue-router'; const router = useRouter(); const route = useRoute();
import { apiPostGet } from "@/api/board";

const post_id = Number(route.params.post_id);

// ----------

interface PostWriteForm_intf {
  setFormData: (data: PostWriteData) => Promise<void>
}
const postWriteForm = ref<PostWriteForm_intf>() as Ref<PostWriteForm_intf>;

// ----------

onMounted(async () => {
  const dataRaw = await apiPostGet(post_id);
  const postWriteData = PostWriteData.fromY(dataRaw);
  postWriteForm.value.setFormData(postWriteData);
});

async function submitPost(data: Z_PostAdd) {
  try {
    await apiPostEdit(post_id, data);
    router.push('/board');
  } catch (e: any) {
    // TODO exception
    // error.value = e?.message || 'Failed to submit post.'
  } finally {
    // loading.value = false
  }
}

</script>
