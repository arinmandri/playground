<template>
  <div class="inputBox input-attachment-file-list">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <div>
      <div v-for="(fap, index) in faps" :key="index">
        <InputAttachmentFile v-model:fileAndPreview="faps[index]" @clear="faps.splice(index, 1)" />
      </div>
      <div v-if="faps.length < props.maxLength">
        <input type="file" accept="image/*" @change="onSelectNewFile" /><!-- 파일 추가 -->
      </div>
    </div>
  </div>
</template>

<!-- TODO: 목록 순서 바꾸기 라이브러리 대충 줏어다 쓸 수 있나? -->

<script setup lang="ts">

import InputAttachmentFile from '@/components/InputImage.vue';

import type { FileAndPreview } from "@/types";

import { ref } from "vue";


const props = defineProps<{
  title?: string;
  fileAndPreviews: FileAndPreview[];
  maxLength: number;// 첨부물 최대 개수
}>();

const emit = defineEmits<{
  (e: 'update:fileAndPreviews', exportProps: FileAndPreview[]): void;
}>();


const faps = ref<FileAndPreview[]>([]);

function onSelectNewFile(event: Event) {
  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  if (selectedFile) {
    faps.value.push({
      newFile: selectedFile,
      preview: URL.createObjectURL(selectedFile),
      fieldValue: null,
    });
    emit('update:fileAndPreviews', faps.value);
  }

  target.value = '';// 새 파일을 선택할 수 있도록 초기화
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}
</style>


