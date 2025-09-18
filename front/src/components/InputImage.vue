<!--
form 속에 파일 1개 첨부 부분.
기존 파일 불러오거나 새 파일 선택, 미리보기 제공.


사용 예시
import type { FileAndPreview } from '@/types';
import { THIS } from '@/components/{ THIS }.vue';
<{ THIS } :title="'프사'" v-model:fileAndPreview="fileField1" />
-->
<template>
  <div class="inputBox input-attachment-file">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <label>
      <input type="file" accept="image/*" class="hidden" @change="onFileChange" />
      <span>파일 선택</span>
    </label>

    <img v-if="props.fileAndPreview.preview" :src="props.fileAndPreview.preview" alt="미리보기" />

    <button type="button" @click="clearFile">
      파일 삭제
    </button>

    <button type="button" @click="resetFile">
      돌림
    </button>

    <div class="">
      <p><b>fieldValue</b>: {{ props.fileAndPreview.fieldValue }}</p>
      <p><b>preview</b>: {{ props.fileAndPreview.preview }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">

import { FileAndPreview } from "@/types";



const props = defineProps<{
  title?: string;
  fileAndPreview: FileAndPreview;
}>();

const emit = defineEmits<{
  (e: 'update:fileAndPreview', exportProps: FileAndPreview | null): void;
}>();

function onFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  if (selectedFile) {
    const temp = props.fileAndPreview;
    temp.setFile(selectedFile);
    emit('update:fileAndPreview', temp as FileAndPreview);
  }
}

function clearFile() {
  const temp = props.fileAndPreview;
  temp.clear();
  emit('update:fileAndPreview', temp as FileAndPreview);
}

function resetFile() {
  const temp = props.fileAndPreview;
  temp.reset();
  emit('update:fileAndPreview', temp);
}
</script>

<style scoped>
.inputTitle {
  font-weight: bold;
  font-size: 80%;
}
</style>


