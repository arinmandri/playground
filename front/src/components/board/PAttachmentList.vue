<template>
  <div class="inputBox input-attachment-file-list">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <div>
      <div v-for="(_, index) in attachments" :key="index">
        <PAttachmentCom v-model:attachment="(attachments[index] as PAttachment)" @clear="attachments.splice(index, 1)" />
      </div>
      <div v-if="attachments.length < props.maxLength">
        <PAttachmentCom :title="'첨부물 추가'" :attachment="getNullAttachment()" @select-new="onSelectNewFile" />
      </div>
    </div>
  </div>
</template>

<!-- TODO: 목록 순서 바꾸기 라이브러리 대충 줏어다 쓸 수 있나? -->

<script setup lang="ts">

import PAttachmentCom from '@/components/board/PAttachment.vue';

import type { PAttachment } from "@/types";
import { ATT_TYPE, FileAndPreview, getNullAttachment } from "@/types";

import { ref, defineExpose } from "vue";


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


const attachments = ref<PAttachment[]>([]);

function onSelectNewFile(newAttachment: PAttachment) {
  attachments.value.push({
    attType: newAttachment.attType,
    attData: {
      typeImage: newAttachment.attData.typeImage?.copy() || null,
      typeFile: newAttachment.attData.typeFile?.copy() || null
    },
  });
  emit('update:attachments', attachments.value as PAttachment[]);// TODO ?????????? 돌았나왜갑자기 강제캐스팅없으면안됨?
}

function uploadFiles() {
  console.log('============== uploadFiles')// TODO
  emit('update:attachments', attachments.value as PAttachment[]);// TODO ?????????? 돌았나왜갑자기 강제캐스팅없으면안됨?
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}
</style>


