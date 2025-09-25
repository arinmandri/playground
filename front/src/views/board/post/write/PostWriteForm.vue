<template>
  <form @submit.prevent="submitPost">
    <textarea v-model="content" type="text">뭐 쓸라고 했더라</textarea>
    <PAttachmentListForm ref="attachmentsComp" :title="'첨부파일'" v-model:attachments="(attachments as PAttachment[])"
      :maxLength="5" />
    <button type="submit">라고 쓰기</button>
  </form>
</template>

<script lang="ts" setup>

import PAttachmentListForm from '@/views/board/post/comp/PAttachmentListForm.vue';

import { PAttachment } from "@/views/board/services/types";
import { type Z_PostAdd } from "@/api/board";
import { toApiSendingFormOfAtt } from '@/api/board';

import { ref, type Ref } from 'vue'

const emit = defineEmits<{
  (e: 'submit', exportProps: Z_PostAdd): void;
}>();

interface PAttachmentList {
  uploadFiles: () => Promise<void>
}

const attachmentsComp = ref<PAttachmentList>() as Ref<PAttachmentList>;

const attachments = ref<PAttachment[]>([]) as Ref<PAttachment[]>;
const content = ref('')


async function submitPost(){
  await attachmentsComp.value.uploadFiles();
  const attsToSend = attachments.value.map(attRaw => toApiSendingFormOfAtt(attRaw));
  emit('submit', {
    content: content.value,
    attachments: attsToSend,
  });
  content.value = ''
}

// TODO loading?
</script>
