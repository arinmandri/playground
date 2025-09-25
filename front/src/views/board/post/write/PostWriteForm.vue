<template>
  <form @submit.prevent="submitPost">
    <textarea v-model="formData.content" type="text">뭐 쓸라고 했더라</textarea>
    <PAttachmentListForm ref="pAttachmentListForm" :title="'첨부파일'"
      v-model:attachments="(formData.attachments as PAttachmentAddData[])" :maxLength="5" />
    <button type="submit">라고 쓰기</button>
  </form>
</template>

<script lang="ts" setup>

import type { PostWriteData } from './types';

import PAttachmentListForm from '@/views/board/post/comp/PAttachmentListForm.vue';

import { PAttachmentAddData } from "@/views/board/post/comp/types";
import { type Z_PostAdd } from "@/api/board";

import { ref, type Ref, defineExpose } from 'vue'

const emit = defineEmits<{
  (e: 'submit', exportProps: Z_PostAdd): void;
}>();

defineExpose({
  setFormData
});

// ----------

const formData = ref<PostWriteData>({
  content: '',
  attachments: [],
}) as Ref<PostWriteData>;

interface PAttachmentListForm_intf {
  uploadFiles: () => Promise<void>
}
const pAttachmentListForm = ref<PAttachmentListForm_intf>() as Ref<PAttachmentListForm_intf>;

// ----------

function setFormData(data: PostWriteData) {
  formData.value = data;
}

async function submitPost() {
  await pAttachmentListForm.value.uploadFiles();
  const attsToSend = formData.value.attachments.map(attRaw => attRaw.toZForm()).filter(att => att != null);
  emit('submit', {
    content: formData.value.content,
    attachments: attsToSend,
  });
  formData.value.content = ''
  formData.value.attachments = [];
}

// TODO loading?
</script>
