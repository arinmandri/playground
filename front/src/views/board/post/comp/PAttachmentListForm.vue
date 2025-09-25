<template>
  <div class="inputBox input-attachment-file-list">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <div>
      <div v-for="(_, index) in attachments" :key="index">
        <PAttachmentForm v-model:attachment="attachments[index]" @clear="attachments.splice(index, 1)" />
      </div>
      <div v-if="attachments.length < props.maxLength">
        <PAttachmentForm :title="'첨부물 추가'" :attachment="PAttachment.newOne()" @select-new="onSelectNewFile" />
      </div>
    </div>
  </div>
</template>

<!-- TODO: 목록 순서 바꾸기 라이브러리 대충 줏어다 쓸 수 있나? -->

<script setup lang="ts">

import api from '@/api/api';
import PAttachmentForm from '@/views/board/post/comp/PAttachmentForm.vue';

import { PAttachment } from "@/views/board/services/types";

import { defineExpose } from "vue";


const props = defineProps<{
  title?: string;
  attachments: PAttachment[];
  maxLength: number;// TODO 첨부물 최대 개수 백엔드랑 어케 일원화함
}>();

const emit = defineEmits<{
  (e: 'update:attachments', exportProps: PAttachment[]): void;
}>();

defineExpose({
  uploadFiles
});


function onSelectNewFile(newAttachment: PAttachment) {
  const attachments = props.attachments;
  attachments.push(newAttachment.copy());
  emit('update:attachments', attachments);
}

function uploadFiles(): Promise<void> {
  const attachments = props.attachments;

  const fs = [] as File[];
  const atts = [] as PAttachment[];
  attachments.forEach((att) => {
    const f = att.getFileIfExists();
    if (f != null) {
      fs.push(f);
      atts.push(att);
    }
  });
  if (fs.length > 0) {
    return new Promise((resolve) => {
      api.uploadFiles(fs).then((res) => {
        const ltfs = res.data;// 길이 = atts 길이
        for (let i = 0; i < atts.length; i += 1) {
          const ltf = ltfs[i];
          const att = atts[i];
          att.setFileIfSettable(ltf.id);
          // TODO 오류시 처리 뭐 해야 됨 진짜
        }
        emit('update:attachments', attachments);
        resolve();
      });
    });
  }
  return new Promise((resolve) => { resolve(); });
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}
</style>


