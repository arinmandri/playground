<template>
  <div>
    <PAttachmentComp :attachment="attachment.data" />
    <button type="button" @click="emit('clear');">
      파일 삭제
    </button>
  </div>
</template>

<script setup lang="ts">

import PAttachmentComp from './PAttachmentComp.vue';

import type { PAttachmentOldData } from './types';

const props = defineProps<{
  attachment: PAttachmentOldData;
}>();

const emit = defineEmits<{
  (e: 'clear'): void;
}>();

</script>
