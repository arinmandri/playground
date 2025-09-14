<!--
form 속에 파일 1개 첨부 부분.
기존 파일 불러오거나 새 파일 선택, 미리보기 제공.
-->
<template>
  <div class="inputBox input-attachment-file">
    <p class="inputTitle">{{ props.title }}</p>
    <label>
      <input type="file" accept="image/*" class="hidden" @change="onFileChange" ref="fileInput" />
      <span>파일 선택</span>
    </label>

    <img v-if="props.fileAndPreview.preview" :src="props.fileAndPreview.preview" alt="미리보기" />

    <button type="button" @click="clearFile">
      파일 삭제
    </button>

    <div class="hidden">
      <p><b>fieldValue</b>: {{ props.fileAndPreview.fieldValue }}</p>
      <p><b>preview</b>: {{ props.fileAndPreview.preview }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">

import type { FileAndPreview } from "@/types";
import { FileAndPreviewDefaultInitial } from '@/types';

import { ref } from "vue";


const props = defineProps<{
  title: string;
  fileAndPreview: FileAndPreview;
}>();

const internalProps = ref<FileAndPreview>(props.fileAndPreview);

const fileInput = ref<HTMLInputElement | null>(null);

const emit = defineEmits<{
  (e: 'update:fileAndPreview', exportProps: FileAndPreview | null): void;
}>();

function onFileChange(event: Event) {
  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  internalProps.value.newFile = selectedFile;

  if (selectedFile) {
    const reader = new FileReader();
    reader.onload = () => {
      internalProps.value.preview = reader.result as string;
    };
    reader.readAsDataURL(selectedFile);
    emit('update:fileAndPreview', internalProps.value);
  }
}

function clearFile() {
  if (fileInput.value) {
    fileInput.value.value = "";
  }
  // XXX internalProps.value = FileAndPreviewDefaultInitial 따위는 안 돼 거지같다.
  internalProps.value.newFile = null;
  internalProps.value.preview = null;
  internalProps.value.fieldValue = '';
  emit('update:fileAndPreview', FileAndPreviewDefaultInitial);
}
</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}
</style>


