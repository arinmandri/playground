<template>
  <div class="edit-member">
    <h2>내 정보 바꾸기</h2>
    <form @submit.prevent="submitForm">
      <InputText :title="'별명'" v-model:textValue="form.nick" :isRequired="true" />
      <InputText :title="'이메일 주소'" v-model:textValue="form.email" :isRequired="false" />
      <InputImage :title="'프사'" v-model:fileAndPreview="propic" />
      <button type="submit" :disabled="loading">저장</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">Profile updated successfully!</div>
  </div>
</template>

<script setup lang="ts">

import InputText from '@/components/InputText.vue';
import InputImage from '@/components/InputImage.vue';

import { FileAndPreview } from '@/types';
import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();
import api from '@/api/axiosInstance';

import { ref, type Ref, onMounted } from 'vue';

const form = ref({
  nick: '',
  email: '',
})

const propic = ref<FileAndPreview>(FileAndPreview.getNull()) as Ref<FileAndPreview>;

const loading = ref(false)// TODO 이거 로딩화면 만듦?
const error = ref('')
const success = ref(false)

onMounted(() => {
  fetchMemberInfo();
});

async function fetchMemberInfo() {
  loading.value = true
  error.value = ''
  try {
    const { data } = await api.get('/member/me');
    form.value.nick = data.nick || '';
    form.value.email = data.email || '';
    propic.value = data.propic == null
      ? FileAndPreview.newOne()
      : FileAndPreview.ofExisting(data.propic);
  } catch (err) {
    error.value = 'Failed to load member info.'
  } finally {
    loading.value = false
  }
}

async function submitForm() {
  loading.value = true;
  error.value = '';
  success.value = false;

  //// 프사 파일 업로드
  if (propic.value.hasNewFile) {
    try {
      const { id: fileId } = (await api.uploadFile(propic.value.fileNN)).data;
      propic.value.setTempFileId(fileId);
    } catch (err) {
      error.value = 'Failed to upload profile picture.';
      loading.value = false;
      return;
    }
  }

  try {
    await api.post('/member/me/edit', {
      nick: form.value.nick,
      email: form.value.email,
      propic: propic.value.fieldValue,
    });
    success.value = true;
    authStore.whoami();
    // XXX 이 페이지도 새로고침 해야 하나?
  } catch (err) {
    error.value = 'Failed to update member info.';
  } finally {
    loading.value = false;
  }
}
</script>

<style scoped>
.edit-member {
  max-width: 400px;
  margin: 2rem auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 8px;
}

.error {
  color: red;
  margin-top: 1rem;
}

.success {
  color: green;
  margin-top: 1rem;
}
</style>