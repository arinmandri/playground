<template>
  <div class="inputBox input-attachment-file-list">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <div>
      <div v-for="(_, index) in attachments" :key="index">
        <PAttachmentCom v-model:attachment="attachments[index]" @clear="attachments.splice(index, 1)" />
      </div>
      <div v-if="attachments.length < props.maxLength">
        <PAttachmentCom :title="'첨부물 추가'" :attachment="PAttachment.getNull()" @select-new="onSelectNewFile" />
      </div>
    </div>
  </div>
</template>

<!-- TODO: 목록 순서 바꾸기 라이브러리 대충 줏어다 쓸 수 있나? -->

<script setup lang="ts">

import api from '@/api/axiosInstance';
import PAttachmentCom from '@/components/board/PAttachment.vue';

import { PAttachment } from "@/types";

import { defineExpose } from "vue";


const props = defineProps<{
  title?: string;
  attachments: PAttachment[];
  maxLength: number;// 첨부물 최대 개수
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

function uploadFiles() {
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
    api.uploadFiles(fs).then((res) => {
      const ltfs = res.data;// 길이 = atts 길이
      for (let i = 0; i < atts.length; i += 1) {
        const ltf = ltfs[i];
        const att = atts[i];
        att.setFileIfSettable(ltf.id);
        // TODO 오류시 처리 어케함 진짜
      }
      emit('update:attachments', attachments);
    });
  }
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}
</style>


