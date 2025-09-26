<template>
  <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
  <p class="hidden">att type: {{ props.attachment.type }}</p>
  <PAttachmentAddForm v-if="attachment.type == NOO_TYPE.new"
    v-model:attachment="(attachment as PAttachmentNewData).content" @select-new="(a) => { emit('select-new', a) }"
    @update:attachment="(a) => { emit('update:attachment', a) }" @clear="emit('clear')" />
  <PAttachmentOldForm v-if="attachment.type == NOO_TYPE.old" />
</template>

<script setup lang="ts">

import PAttachmentAddForm from '@/views/board/post/comp/PAttachmentAddForm.vue';
import PAttachmentOldForm from '@/views/board/post/comp/PAttachmentOldForm.vue'

import { PAttachmentAddData, type PAttachmentNooData, PAttachmentNewData, NOO_TYPE } from "@/views/board/post/comp/types";

const props = defineProps<{
  title?: string;
  attachment: PAttachmentNooData;
}>();

const emit = defineEmits<{
  (e: 'update:attachment', exportProps: PAttachmentAddData): void;// just transmit
  (e: 'select-new', exportProps: PAttachmentAddData): void;// just transmit
  (e: 'clear'): void;// just transmit
}>();
</script>
