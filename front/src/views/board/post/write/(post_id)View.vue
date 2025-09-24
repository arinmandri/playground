<template>
  <div class="write-post-view">
    <h1>고쳐쓰기</h1>
    <PostWriteForm @submit="submitPost"></PostWriteForm>
  </div>
</template>

<script setup lang="ts">

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import { type Z_PostAdd } from "@/views/board/services/apicall";

import { onMounted } from "vue";
import { useRoute } from 'vue-router';
import { apiPostGet } from "@/views/board/services/apicall";

const route = useRoute();
const post_id = Number(route.params.post_id);

onMounted(async () => {
  const a = await apiPostGet(post_id);
  console.log('0000000000000000000', a);// TOOD
});

async function submitPost(data: Z_PostAdd) {
  console.log("TODO")// TODO
}

</script>
