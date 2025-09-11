<!-- 이것도 주소가 admin 홈 아니고 로그인 페이지 주소 따로 있어야 하나  -->
<template>
  <div class="login-view">
    <h1>Hello Admin</h1>
    <form @submit.prevent="onSubmit" class="login-form">
      <div>
        <label for="keyname">KEY</label>
        <input id="keyname" v-model="form.keyname" type="text" required autocomplete="username" />
      </div>
      <div>
        <label for="password">비밀번호</label>
        <input id="password" v-model="form.password" type="password" required autocomplete="current-password" />
      </div>
      <button type="submit" :disabled="loading">
        {{ loading ? 'Logging in...' : 'Login' }}
      </button>
      <div v-if="error" class="error">{{ error }}</div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();

import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'; const router = useRouter(); const route = useRoute();


interface LoginForm {
  keyname: string
  password: string
}

const form = ref<LoginForm>({
  keyname: '',
  password: ''
})

const loading = ref(false)
const error = ref('')

const onSubmit = async () => {
  error.value = ''
  loading.value = true
  try {
    await authStore.loginAsAdmin(form.value.keyname, form.value.password);
    router.push(route.query.redirect?.toString() || "/admin/member");
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Login failed'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-view {
  max-width: 400px;
  margin: 60px auto;
  padding: 32px;
  border: 1px solid #eee;
  border-radius: 8px;
  background: #fff;
}

.login-form>div {
  margin-bottom: 16px;
}

label {
  display: block;
  margin-bottom: 4px;
  font-weight: 600;
}

input {
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}

button {
  width: 100%;
  padding: 10px;
  font-size: 16px;
}

.error {
  color: #d32f2f;
  margin-top: 12px;
  text-align: center;
}
</style>