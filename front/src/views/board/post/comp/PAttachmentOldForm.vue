<template>
  <div>기존 첨부파일!!!
    {{ attachment.data }}
    <button type="button" @click="emit('clear');">
      파일 삭제
    </button>
  </div>
</template>

<script setup lang="ts">

import type { PAttachmentOldData } from './types';

const props = defineProps<{
  attachment: PAttachmentOldData;
}>();

const emit = defineEmits<{
  (e: 'clear'): void;
}>();

</script>
