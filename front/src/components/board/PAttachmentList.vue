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

import PAttachmentCom from '@/components/board/PAttachment.vue';

import { PAttachment } from "@/types";

import { ref, type Ref, defineExpose } from "vue";


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


const attachments = ref<PAttachment[]>([]) as Ref<PAttachment[]>;

function onSelectNewFile(newAttachment: PAttachment) {
  attachments.value.push(newAttachment.copy());
  emit('update:attachments', attachments.value);
}

function uploadFiles() {
  console.log('============== uploadFiles')// TODO
  emit('update:attachments', attachments.value);
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}
</style>


