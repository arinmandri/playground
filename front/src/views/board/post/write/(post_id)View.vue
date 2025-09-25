<template>
  <div class="write-post-view">
    <h1>고쳐쓰기</h1>
    <PostWriteForm ref="postWriteForm" @submit="submitPost"></PostWriteForm>
  </div>
</template>

<script setup lang="ts">

import PostWriteForm from "@/views/board/post/write/PostWriteForm.vue"

import type { Y_PostDetail, Y_PAttachment, Z_PostAdd, Y_PAttachmentImage, Y_PAttachmentFile } from "@/api/board";
import { type PostWrite, ATT_TYPE, PAttachment } from "@/views/board/services/types";

import { ref, type Ref, onMounted } from "vue";
import { useRoute } from 'vue-router'; const route = useRoute();
import { apiPostGet } from "@/api/board";
import { FileAndPreview } from "@/types";

const post_id = Number(route.params.post_id);

// ----------

interface PostWriteForm_intf {
  setFormData: (data: PostWrite) => Promise<void>
}
const postWriteForm = ref<PostWriteForm_intf>() as Ref<PostWriteForm_intf>;

// ----------

onMounted(async () => {
  const dataRaw = await apiPostGet(post_id);
  postWriteForm.value.setFormData(
    convertY_PostDetail_to_PostWrite(dataRaw)
  );
});

async function submitPost(data: Z_PostAdd) {
  console.log("TODO")// TODO
}

function convertY_PostDetail_to_PostWrite(dataRaw: Y_PostDetail): PostWrite {
  const content = dataRaw.content;
  const attachments = dataRaw.attachments.map(convert_Y_PAttachment_to_PAttachment);

  return {
    content,
    attachments,
  }
}

function convert_Y_PAttachment_to_PAttachment(dataRaw: Y_PAttachment): PAttachment {
  const type = dataRaw.type;
  if (type == ATT_TYPE.image) {
    const dataImageRaw = dataRaw as Y_PAttachmentImage;
    return PAttachment.ofExisting(type, FileAndPreview.ofExisting(dataImageRaw.url));
  }
  if (type == ATT_TYPE.file) {
    const dataFileRaw = dataRaw as Y_PAttachmentFile;
    return PAttachment.ofExisting(type, FileAndPreview.ofExisting(dataFileRaw.url));
  }
  throw new Error('convert_Y_PAttachment_to_PAttachment: unknown type');
}

</script>
