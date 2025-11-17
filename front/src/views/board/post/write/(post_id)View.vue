<template>
  <div class="write-post-view">
    <h1>고쳐쓰기</h1>
    <PostWriteForm ref="postWriteForm" @submit="submitPost"></PostWriteForm>
  </div>
</template>

<script setup lang="ts">

import { PostWriteData } from './types';

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import type { ReqBody_apiPostAdd } from "@/api/schemas";

import { apiPostGet, apiPostEdit } from "@/api/operations";
import { MsgClass, useMsgStore } from '@/stores/globalMsg'; const msgStore = useMsgStore();

import { ref, type Ref, onMounted } from "vue";
import { useRouter, useRoute } from 'vue-router'; const router = useRouter(); const route = useRoute();

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

async function submitPost(data: ReqBody_apiPostAdd) {
  try {
    await apiPostEdit(post_id, data);
    msgStore.addMsg(MsgClass.INFO, '게시글 수정됨.');
    router.push('/board');
  } catch (e: any) {
    msgStore.addMsg(MsgClass.ERROR, '왠지 실패함.');
  }
}

</script>
