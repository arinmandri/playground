<template>
  <div class="edit-member">
    <h2>Edit Member Info</h2>
    <form @submit.prevent="submitForm">
      <div>
        <label for="input-nick">Nickname:</label>
        <input id="input-nick" v-model="form.nick" type="text" required />
      </div>
      <div>
        <label for="input-email">Email:</label>
        <input id="input-email" v-model="form.email" type="email" />
      </div>
      <div>
        <label for="input-propic">Profile Picture:</label>
        <input id="input-propic" type="file" @change="onPropicFileChange" accept="image/*" />
        <div v-if="propicPreview">
          <img :src="propicPreview" alt="프사" style="max-width: 100px; max-height: 100px;" />
        </div>
        <button type="button" @click="removePropic">프사 지우기</button>
        // TEST<br>
        propic: {{ form.propic }}<br>
        propicPreview: {{ propicPreview }}<br>
      </div>
      <button type="submit" :disabled="loading">Save</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">Profile updated successfully!</div>
  </div>
</template>

<script setup lang="ts">

import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();

import { ref, onMounted } from 'vue';
import api from '@/api/axiosInstance';

const SERVER_TEMP_FILE_ID_PREFIX = '!';

const form = ref({
  nick: '',
  email: '',
  propic: '',
})
const propicPreview = ref('');
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
    form.value.propic = data.propic || '';
    propicPreview.value = data.propic;
  } catch (err) {
    error.value = 'Failed to load member info.'
  } finally {
    loading.value = false
  }
}

const propicRender = new FileReader();
propicRender.onload = function (e: any) {
  propicPreview.value = e.target.result;
}

let propicFile: File | null = null;

function onPropicFileChange(e: any) {
  const file = e.target.files[0];
  if (!file) return;
  propicFile = file;
  propicRender.readAsDataURL(file);
}

function removePropic() {
  propicFile = null;
  propicPreview.value = '';
  form.value.propic = '';
}

async function submitForm() {
  loading.value = true;
  error.value = '';
  success.value = false;

  //// 프사 파일 업로드
  if (propicFile) {
    try {
      const { id: fileId } = (await api.uploadFile(propicFile)).data;
      form.value.propic = SERVER_TEMP_FILE_ID_PREFIX + fileId;
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
      propic: form.value.propic,
    });
    success.value = true;
    authStore.whoami();
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

.edit-member label {
  display: block;
  margin-bottom: 0.25rem;
}

.edit-member input[type="text"],
.edit-member input[type="email"] {
  width: 100%;
  margin-bottom: 1rem;
  padding: 0.5rem;
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