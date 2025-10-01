<template>
  <div id="MemberEditView">
    <h2>내 정보 바꾸기</h2>
    <form @submit.prevent="submitForm">
      <InputText :title="'별명'" v-model:textValue="form.nick" :isRequired="true" />
      <InputText :title="'이메일 주소'" v-model:textValue="form.email" :isRequired="false" />
      <InputImage :title="'프사'" v-model:fileAndPreview="propic" />
      <button type="submit">저장</button>
    </form>
  </div>
</template>

<script setup lang="ts">

import InputText from '@/components/InputText.vue';
import InputImage from '@/components/InputImage.vue';

import { FileAndPreview } from '@/types';
import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();
import { MsgClass, useMsgStore } from '@/stores/globalMsg'; const msgStore = useMsgStore();
import api from '@/api/api';

import { ref, type Ref, onMounted } from 'vue';

const form = ref({
  nick: '',
  email: '',
})

const propic = ref<FileAndPreview>(FileAndPreview.newOne()) as Ref<FileAndPreview>;

onMounted(() => {
  fetchMemberInfo();
});

async function fetchMemberInfo() {
  try {
    const { data } = await api.get('/member/me');
    form.value.nick = data.nick || '';
    form.value.email = data.email || '';
    propic.value = data.propic == null
      ? FileAndPreview.newOne()
      : FileAndPreview.ofExisting(data.propic);
  } catch (err) {
    msgStore.addMsg(MsgClass.ERROR, '회원정보를 가져오지 못했습니다.');
  }
}

async function submitForm() {

  //// 프사 파일 업로드
  if (propic.value.hasNewFile) {
    try {
      const { id: fileId } = (await api.uploadFile(propic.value.fileNN)).data;
      propic.value.setTempFileId(fileId);
    } catch (err) {
      msgStore.addMsg(MsgClass.ERROR, '프로필사진 이미지 업로드에 실패했습니다.');
      return;
    }
  }

  try {
    await api.post('/member/me/edit', {
      nick: form.value.nick,
      email: form.value.email,
      propic: propic.value.fieldValue,
    });
    msgStore.addMsg(MsgClass.INFO, '내 정보를 고쳤습니다.');
    authStore.whoami();
    // XXX 이 페이지도 새로고침 해야 하나?
  } catch (err) {
    msgStore.addMsg(MsgClass.ERROR, '내 정보가 고쳐지지 않았습니다.');
  }
}
</script>

<style scoped>
#MemberEditView {
  max-width: 400px;
  margin: 2rem auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
}
</style>