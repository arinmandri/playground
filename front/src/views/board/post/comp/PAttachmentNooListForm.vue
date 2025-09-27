<template>
  <div class="inputBox input-attachment-file-list">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <div>
      <ul>
        <li v-for="(_, index) in attachments" :key="index" class="attachments-li">
          <PAttachmentNooForm v-model:attachment="attachments[index]" @clear="attachments.splice(index, 1)" />
        </li>
      </ul>
      <div v-if="attachments.length < props.maxLength">
        <PAttachmentAddForm :title="'첨부물 추가'" :attachment="PAttachmentAddData.newOne()" @select-new="onSelectNewFile" />
      </div>
    </div>
  </div>
</template>

<!-- TODO: 목록 순서 바꾸기 라이브러리 대충 줏어다 쓸 수 있나? -->

<script setup lang="ts">

import PAttachmentNooForm from '@/views/board/post/comp/PAttachmentNooForm.vue';
import PAttachmentAddForm from '@/views/board/post/comp/PAttachmentAddForm.vue';

import api from '@/api/api';

import { type PAttachmentNooData, PAttachmentNewData, PAttachmentAddData, NOO_TYPE } from "./types";

import { defineExpose } from "vue";


const props = defineProps<{
  title?: string;
  attachments: PAttachmentNooData[];
  maxLength: number;// TODO 첨부물 최대 개수 백엔드랑 어케 일원화함
}>();

const emit = defineEmits<{
  (e: 'update:attachments', exportProps: PAttachmentNooData[]): void;
}>();

defineExpose({
  uploadFiles
});


function onSelectNewFile(newAttachment: PAttachmentAddData) {
  const attachments = props.attachments;
  attachments.push(
    PAttachmentNewData.ofNew(newAttachment.copy())
  );
  emit('update:attachments', attachments);
}

function uploadFiles(): Promise<void> {
  const attachmentsNew = props.attachments
    .filter(att => att.type == NOO_TYPE.new) as PAttachmentNewData[];

  //// 새 파일 있는 거만 모아
  const fs = [] as File[];
  const attAdds = [] as PAttachmentAddData[];
  attachmentsNew.forEach((attNew) => {
    const attAdd = attNew.content;
    const f = attAdd.getFileIfExists();
    if (f != null) {
      fs.push(f);
      attAdds.push(attAdd);
    }
  });
  if (fs.length > 0) {// 업로드할 거 있음
    return new Promise((resolve) => {
      //// 업로드 하고 결과값 적용
      api.uploadFiles(fs).then((res) => {
        const ltfs = res.data;// 길이 = attAdds 길이
        for (let i = 0; i < attAdds.length; i += 1) {
          const ltf = ltfs[i];
          const att = attAdds[i];
          att.setFileIfSettable(ltf.id);
          // TODO 오류시 처리 뭐 해야 됨 진짜
        }
        emit('update:attachments', props.attachments);
        resolve();
      });
    });
  }
  return new Promise((resolve) => { resolve(); });
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
}

.attachments-li {
  border: 2px dashed #8888
}
</style>
