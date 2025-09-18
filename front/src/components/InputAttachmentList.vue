<template>
  <div class="inputBox input-attachment-file-list">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <div>
      <div v-for="(_, index) in attachments" :key="index">
        <InputAttachment v-model:attachment="attachments[index]" @clear="attachments.splice(index, 1)" />
      </div>
      <div v-if="attachments.length < props.maxLength">
        <InputAttachment :title="'첨부물 추가'" :attachment="getNullAttachment()" @select-new="onSelectNewFile" />
      </div>
    </div>
  </div>
</template>

<!-- TODO: 목록 순서 바꾸기 라이브러리 대충 줏어다 쓸 수 있나? -->

<script setup lang="ts">

import InputAttachment from '@/components/InputAttachment.vue';

import type { Attachment } from "@/types";
import { getNullAttachment } from "@/types";

import { ref, defineExpose } from "vue";


const props = defineProps<{
  title?: string;
  attachments: Attachment[];
  maxLength: number;// 첨부물 최대 개수
}>();

const emit = defineEmits<{
  (e: 'update:attachments', exportProps: Attachment[]): void;
}>();

defineExpose({
  uploadFiles
});


const attachments = ref<Attachment[]>([]);

function onSelectNewFile(newAttachment: Attachment) {
  attachments.value.push({
    attType: newAttachment.attType,
    attData: { ...newAttachment.attData },
  });
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


