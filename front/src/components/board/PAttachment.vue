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

import { PAttachment, ATT_TYPE } from "@/types";

const props = defineProps<{
  title?: string;
  attachment: PAttachment;
}>();

const emit = defineEmits<{
  (e: 'update:attachment', exportProps: PAttachment): void;
  (e: 'select-new', exportProps: PAttachment): void;
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
.inputTitle {
  font-weight: bold;
}

label {
  border: 1px dotted grey;
}
</style>
