<template>
  <div class="write-post-view">
    <h1>고쳐쓰기</h1>
    <PostWriteForm ref="postWriteForm" @submit="submitPost"></PostWriteForm>
  </div>
</template>

<script setup lang="ts">

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import type { Z_PostAdd } from "@/api/board";
import { PostWrite } from "@/views/board/services/types";

import { ref, type Ref, onMounted } from "vue";
import { useRoute } from 'vue-router'; const route = useRoute();
import { apiPostGet } from "@/api/board";

const post_id = Number(route.params.post_id);

// ----------

interface PostWriteForm_intf {
  setFormData: (data: PostWrite) => Promise<void>
}
const postWriteForm = ref<PostWriteForm_intf>() as Ref<PostWriteForm_intf>;

// ----------

onMounted(async () => {
  const dataRaw = await apiPostGet(post_id);
  postWriteForm.value.setFormData(
    PostWrite.fromY(dataRaw)
  );
});

async function submitPost(data: Z_PostAdd) {
  console.log("TODO")// TODO
}

</script>
