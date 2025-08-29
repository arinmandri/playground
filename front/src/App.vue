<template>
  <div>
    <nav>
      <ul id="globalMenu">
        <li><router-link to="/">홈</router-link></li>
        <li><router-link to="/board">게시판</router-link></li>
        <li><router-link to="/member/join">가입</router-link></li>
        <li><router-link to="/member/login">로그인</router-link></li>
      </ul>
      <div id="user">
        <div v-if="isLoggedIn">
          <span>로그인함</span>
          <button @click="logout">로그아웃</button>
        </div>
        <div v-else>
          <span>비회원</span>
        </div>
      </div>
    </nav>

    <div id="routerViewDiv">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();

import { onMounted } from "vue";
import { useRouter } from 'vue-router'; const router = useRouter();
import { storeToRefs } from "pinia";

const { isLoggedIn } = storeToRefs(authStore);

onMounted(async () => {
  console.log('App.vue mounted');
});

const logout = () => {
  authStore.logout();
  router.push('/member/login');
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');

body {
  min-width: 320px;
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #f5f5f5;
  color: #333;
  line-height: 1.6;
}

* {
  margin: 0px;
  padding: 0px;
  border: 0px solid black;
}

nav {
  background-color: #eee;
  padding: 10px;
  position: sticky;
  top: 0;
}

#globalMenu {
  list-style: none;
  display: flex;
  gap: 10px;
}

#routerViewDiv {
  max-width: 840px;
  margin: 0 auto;
  padding: 20px 0;
  box-shadow: 0px 2px 8px rgba(0,0,0,0.3);
}
#routerViewDiv > * {
  margin: 0 20px;
}
</style>
