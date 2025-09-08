<template>
  <h1>{{ user.nick }} 님의 본거지</h1>
  <router-link to="/member/edit">내 정보 고쳐쓰기</router-link>
  <button @click="logout">로그아웃</button>
</template>

<script setup lang="ts">

import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();
import { useRouter } from 'vue-router'; const router = useRouter();
import { storeToRefs } from "pinia";

const { user } = storeToRefs(authStore);

const logout = () => {
  authStore.logout();
  router.push('/member/login');
};
</script>