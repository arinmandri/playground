<template>
  <img v-if="props.attachment.type == PAttachmentType.image" :src="(props.attachment as Y_PAttachmentImage).url" />
  <p v-if="props.attachment.type == PAttachmentType.file">
    <a :href="(props.attachment as Y_PAttachmentFile).url" download>
      파일 {{ (props.attachment as Y_PAttachmentFile).size }}KB
    </a>
  </p>
</template>

<script setup lang="ts">

import type { Y_PAttachment, Y_PAttachmentImage, Y_PAttachmentFile } from "@/api/schemas";// TODO Y_PAttachmentImage, Y_PAttachmentFile
import { PAttachmentType } from "@/api/schemas";

const props = defineProps<{
  attachment: Y_PAttachment;
}>();

</script>
