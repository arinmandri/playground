<template>
  <h1>((회원 닉네임)) 본거지</h1><!-- TODO -->
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