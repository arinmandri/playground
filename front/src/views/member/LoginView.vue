<template>
  <div class="login-view">
    <form @submit.prevent="onSubmit" class="login-form">
      <div>
        <label for="keyname">Key</label>
        <input id="keyname" v-model="form.keyname" type="text" required autocomplete="username" />
      </div>
      <div>
        <label for="password">비밀번호</label>
        <input id="password" v-model="form.password" type="password" required autocomplete="current-password" />
      </div>
      <button type="submit">로그인</button>
    </form>
    <router-link to="/member/join">가입</router-link>
  </div>
</template>

<script lang="ts" setup>

import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();
import { MsgClass, useMsgStore } from '@/stores/globalMsg'; const msgStore = useMsgStore();

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

const onSubmit = async () => {
  try {
    await authStore.loginWithBasicKey(form.value.keyname, form.value.password);
    router.push(route.query.redirect?.toString() || "/");
  } catch (err: any) {
    msgStore.addMsg(MsgClass.ERROR, '왠지 로그인에 실패함.');
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
</style>