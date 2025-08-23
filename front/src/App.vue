<template>
  <div>
    <nav>
      <ul id="globalMenu">
        <li><router-link to="/">홈</router-link></li>
        <li><router-link to="/board">게시판</router-link></li>
        <li><router-link to="/member/join">가입</router-link></li>
        <li><router-link to="/member/login">로그인</router-link></li>
      </ul>
      <div v-if="isLoggedIn">
        <span>로그인함</span>
        <button @click="logout">로그아웃</button>
      </div>
      <div v-else>
        <span>비회원</span>
      </div>
    </nav>

    <router-view />
  </div>
</template>

<script setup lang="ts">
import api from "@/api/axiosInstance";
import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();

import { onMounted } from "vue";
import { useRouter } from 'vue-router'; const router = useRouter();
import { storeToRefs } from "pinia";

const { isLoggedIn } = storeToRefs(authStore);

onMounted(async () => {
});

const logout = () => {
  authStore.logout();
  router.push('/member/login');
};
</script>

<style>
* {
  margin: 0px;
  padding: 0px;
}

nav {
  background-color: #eee;
  margin-bottom: 20px;
  padding: 10px;
}

#globalMenu {
  list-style: none;
  display: flex;
  gap: 10px;
}
</style>
