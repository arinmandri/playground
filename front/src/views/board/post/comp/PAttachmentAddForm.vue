<template>
  <div class="inputBox input-attachment-file">
    <p class="hidden">att type: {{ props.attachment.attType }}</p>

    <div v-if="props.attachment.attType == null">
      <label>
        <input type="file" accept="image/*" class="hidden" @change="onImageSelect" />
        <span>이미지 선택</span>
      </label>
      <label>
        <input type="file" accept="*" class="hidden" @change="onFileSelect" />
        <span>파일 선택</span>
      </label>
    </div>

    <div v-if="props.attachment.attType != null">
      <img class="image-preview" v-if="props.attachment.attType == PAttachmentType.image"
        :src="props.attachment.attData.typeImage?.preview" alt="이미지 미리보기" />
      <p class="hidden">선택된 파일: {{ props.attachment.attData.typeFile?.name }}</p>
    </div>

    <button type="button" v-if="props.attachment.attType != null" @click="clearFile">
      파일 삭제
    </button>

    <div class="hidden">
      <div v-if="props.attachment.attType == PAttachmentType.image">
        <p><b>fieldValue</b>: {{ props.attachment.attData.typeImage?.fieldValue }}</p>
        <p><b>preview</b>: {{ props.attachment.attData.typeImage?.preview }}</p>
      </div>
      <div v-if="props.attachment.attType == PAttachmentType.file">
        <p><b>fieldValue</b>: {{ props.attachment.attData.typeFile?.fieldValue }}</p>
        <p><b>preview</b>: {{ props.attachment.attData.typeFile?.preview }}</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import { PAttachmentAddData } from "@/views/board/post/comp/types";
import { PAttachmentType } from "@/api/schemas";

const props = defineProps<{
  attachment: PAttachmentAddData;
}>();

const emit = defineEmits<{
  (e: 'update:attachment', exportProps: PAttachmentAddData): void;
  (e: 'select-new', exportProps: PAttachmentAddData): void;
  (e: 'clear'): void;
}>();

function onImageSelect(event: Event) {
  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  if (selectedFile) {
    const temp = props.attachment;
    temp.setImage(selectedFile);
    emit('update:attachment', temp);
    emit('select-new', temp);
  }
}

function onFileSelect(event: Event) {
  const target = event.target as HTMLInputElement;
  const selectedFile = target.files?.[0] ?? null;

  if (selectedFile) {
    const temp = props.attachment;
    temp.setFile(selectedFile);
    emit('update:attachment', temp);
    emit('select-new', temp);
  }
}

function clearFile() {
  const temp = props.attachment;
  temp.clear();
  emit('update:attachment', temp);
  emit('clear');
}
</script>

<style scoped>
label {
  border: 1px dotted grey;
}

.image-preview {
  max-width: 200px;
  max-height: 200px;
}
</style>
