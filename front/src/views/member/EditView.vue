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
        <input id="input-propic" type="file" @change="onFileChange" accept="image/*" />
        <div v-if="form.propicPreview">
          <img :src="form.propicPreview" alt="Profile Picture" style="max-width: 100px; max-height: 100px;" />
        </div>
        // TEST<br>
        propic: {{ form.propic }}<br>
        propicPreview: {{ form.propicPreview }}<br>
      </div>
      <button type="submit" :disabled="loading">Save</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">Profile updated successfully!</div>
  </div>
</template>

<script setup lang="ts">

import { useAuthStore, getEnsuredAccessToken } from '@/stores/auth'; const authStore = useAuthStore();

import { ref, onMounted } from 'vue';
import api from '@/api/axiosInstance';

const SERVER_TEMP_FILE_ID_PREFIX = '!';

const form = ref({
  nick: '',
  email: '',
  propic: '',
  propicPreview: '',
})
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
    form.value.propicPreview = data.propic;
  } catch (err) {
    error.value = 'Failed to load member info.'
  } finally {
    loading.value = false
  }
}

const propicRender = new FileReader();
propicRender.onload = function (e: any) {
  form.value.propicPreview = e.target.result;
}

async function onFileChange(e: any) {
  // TODO 용량제한
  const file = e.target.files[0]
  if (!file) return
  propicRender.readAsDataURL(file);
  loading.value = true
  error.value = ''
  try {
    const formData = new FormData()
    formData.append('file', file)
    const { id: fileId } = (await api.postFormData(formData)).data;
    form.value.propic = SERVER_TEMP_FILE_ID_PREFIX + fileId;
  } catch (err) {
    error.value = 'Failed to upload profile picture.'
  } finally {
    loading.value = false
  }
}

async function submitForm() {
  loading.value = true
  error.value = ''
  success.value = false
  try {
    await api.post('/member/me/edit', {
      nick: form.value.nick,
      email: form.value.email,
      propic: form.value.propic
    })
    success.value = true
    authStore.whoami();
  } catch (err) {
    error.value = 'Failed to update member info.'
  } finally {
    loading.value = false
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