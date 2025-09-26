<template>
  <img v-if="props.attachment.type == ATT_TYPE.image" :src="(props.attachment as Y_PAttachmentImage).url" />
  <p v-if="props.attachment.type == ATT_TYPE.file">
    <a :href="(props.attachment as Y_PAttachmentFile).url" download>
      파일 {{ (props.attachment as Y_PAttachmentFile).size }}KB
    </a>
  </p>
</template>

<script setup lang="ts">

import { type Y_PAttachment, type Y_PAttachmentImage, type Y_PAttachmentFile } from "@/api/board";
import { ATT_TYPE } from "@/views/board/services/types";

const props = defineProps<{
  attachment: Y_PAttachment;
}>();

</script>
