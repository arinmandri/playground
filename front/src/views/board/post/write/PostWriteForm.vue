<template>
  <form @submit.prevent="submitPost">
    <textarea v-model="formData.content" type="text">뭐 쓸라고 했더라</textarea>
    <PAttachmentNooListForm ref="pAttachmentNooListForm" :title="'첨부파일'"
      v-model:attachments="(formData.attachments as PAttachmentNooData[])" :maxLength="5" />
    <button type="submit">라고 쓰기</button>
  </form>
</template>

<script lang="ts" setup>

import type { PostWriteData } from './types';

import PAttachmentNooListForm from '@/views/board/post/comp/PAttachmentNooListForm.vue';

import { PAttachmentNooData } from "@/views/board/post/comp/types";
import { type Z_PAttachmentNoo, type Z_PostAdd } from "@/api/board";

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

interface PAttachmentNooListForm_intf {
  uploadFiles: () => Promise<void>
}
const pAttachmentNooListForm = ref<PAttachmentNooListForm_intf>() as Ref<PAttachmentNooListForm_intf>;

// ----------

function setFormData(data: PostWriteData) {
  formData.value = data;
}

async function submitPost() {
  await pAttachmentNooListForm.value.uploadFiles();

  const attsToSend = [] as Z_PAttachmentNoo[];
  for (let attRaw of formData.value.attachments) {
    const temp = attRaw.toZForm();
    if (temp != null) attsToSend.push(temp);
  }

  emit('submit', {
    content: formData.value.content,
    attachments: attsToSend,
  });
  formData.value.content = ''
  formData.value.attachments = [];
}

// TODO loading?
</script>
