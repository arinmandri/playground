<!--
-->
<template>
  <div class="inputBox input-attachment-file">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <p>att type: {{ props.attachment.attType }}</p><!-- TEST -->

    <div v-if="props.attachment.attType == null">
      <label>
        <input type="file" accept="image/*" class="hidden" @change="onImageSelect" />
        <span>이미지 선택</span>
      </label>
      <label>
        <input type="file" accept="image/*" class="hidden" @change="onFileSelect" />
        <span>파일 선택</span>
      </label>
    </div>

    <div v-if="props.attachment.attType != null">
      <img v-if="props.attachment.attType == ATT_TYPE.image" :src="props.attachment.attData.typeImage?.preview" alt="이미지 미리보기" />
    </div>

    <button type="button" @click="clearFile">
      파일 삭제
    </button>

    <div><!-- TEST -->
      <div v-if="props.attachment.attType == ATT_TYPE.image">
        <p><b>fieldValue</b>: {{ props.attachment.attData.typeImage?.fieldValue }}</p>
        <p><b>preview</b>: {{ props.attachment.attData.typeImage?.preview }}</p>
      </div>
      <div v-if="props.attachment.attType == ATT_TYPE.file">
        <p><b>fieldValue</b>: {{ props.attachment.attData.typeFile?.fieldValue }}</p>
        <p><b>preview</b>: {{ props.attachment.attData.typeFile?.preview }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import type { Attachment } from "@/types";
import { getFileAndPreviewDefaultInitial, getNullAttachment } from "@/types";
import { ATT_TYPE } from "@/types";

const props = defineProps<{
  title?: string;
  attachment: Attachment;
}>();

let internalData = props.attachment as Attachment;

const emit = defineEmits<{
  (e: 'update:attachment', exportProps: Attachment): void;
  (e: 'select-new', exportProps: Attachment): void;
  (e: 'clear'): void;
}>();

function onImageSelect(event: Event) {
  clearInternal();
  internalData.attType = ATT_TYPE.image;
  internalData.attData.typeImage = getFileAndPreviewDefaultInitial();

  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  internalData.attData.typeImage.newFile = selectedFile;

  if (selectedFile) {
    internalData.attData.typeImage.preview = URL.createObjectURL(selectedFile);
    emit('update:attachment', internalData);
    emit('select-new', internalData);
  }
}

function onFileSelect(event: Event) {
  clearInternal();
  internalData.attType = ATT_TYPE.file;
  internalData.attData.typeFile = getFileAndPreviewDefaultInitial();

  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  internalData.attData.typeFile.newFile = selectedFile;

  if (selectedFile) {
    internalData.attData.typeFile.preview = URL.createObjectURL(selectedFile);
    emit('update:attachment', internalData);
    emit('select-new', internalData);
  }
}

function clearInternal() {
  const keys = Object.keys(props.attachment.attData);
  props.attachment.attData.typeImage = null;
  props.attachment.attData.typeFile = null;
}

function clearFile() {
  internalData = getNullAttachment();
  emit('update:attachment', internalData);
  emit('clear');
}
</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}

label {
  border: 1px dotted grey;
}
</style>
