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
        <input id="input-email" v-model="form.email" type="email" required />
      </div>
      <div>
        <label for="input-propic">Profile Picture:</label>
        <input id="input-propic" type="file" @change="onFileChange" accept="image/*" />
        <div v-if="form.propic">
          <img :src="form.propic" alt="Profile Picture" style="max-width: 100px; max-height: 100px;" />
        </div>
      </div>
      <button type="submit" :disabled="loading">Save</button>
    </form>
    <div v-if="error" class="error">{{ error }}</div>
    <div v-if="success" class="success">Profile updated successfully!</div>
  </div>
</template>

<script setup lang="ts">
// TODO 초기값: 원래 회원 정보
import { ref } from 'vue';
import api from '@/api/axiosInstance';

const SERVER_TEMP_FILE_ID_PREFIX = '!';

const form = ref({
  nick: '',
  email: '',
  propic: ''
})
const loading = ref(false)
const error = ref('')
const success = ref(false)

async function onFileChange(e: any) {
  const file = e.target.files[0]
  if (!file) return
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