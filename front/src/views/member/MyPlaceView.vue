<template>
  <h1>누구 본거지</h1>
  <button @click="logout">로그아웃</button>
</template>

<script setup lang="ts">

import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();
import { useRouter } from 'vue-router'; const router = useRouter();

const logout = () => {
  authStore.logout();
  router.push('/member/login');
};
</script>