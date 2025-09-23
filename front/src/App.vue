<template>
  <nav>
    <ul id="globalMenu">
      <li><router-link to="/">홈</router-link></li>
      <li><router-link to="/board">게시판</router-link></li>
    </ul>
  </nav>

  <div id="user">
    <div v-if="isLoggedIn">
      <router-link to="/member/myplace">
        <span>{{ user.nick }} 님 입장</span>
        <img id="user-propic" v-if="user.propic" :src="user.propic">
      </router-link>
    </div>
    <div v-else>
      <span><router-link to="/member/login">손님 입장하세요</router-link></span>
    </div>
  </div>

  <div id="routerViewDiv">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'; const authStore = useAuthStore();

import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const { isLoggedIn, user } = storeToRefs(authStore);

onMounted(async () => {
  authStore.loadAuthInfoFromLocal();
});
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap');

:root {
  --point-blue: #4a90e2;
}

body {
  min-width: 320px;
  font-family: 'Noto Sans KR', sans-serif;
  background-color: #e8e8e8;
  color: #333;
  line-height: 1.6;
}

#app {
  max-width: 1200px;
  margin: 0 auto;
}

* {
  margin: 0px;
  padding: 0px;
  border: 0px solid black;
}

.hidden {
  display: none;
}

button,
input,
select,
textarea {
  border: 1px solid grey;
}

a {
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

#globalMenu {
  background-color: #fff;
  padding: 10px;
  top: 0;
  font-family: "Do Hyeon", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 160%;
}

#globalMenu a {
  color: unset;
}

#globalMenu {
  list-style: none;
  display: flex;
  gap: 10px;
}

#routerViewDiv {
  margin: 8px auto 20px auto;
  padding: 20px 0;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.3);
  background-color: #f6f6f6;
}

#routerViewDiv>* {
  margin: 0 20px;
}

#user {
  padding: 10px;
  text-align: right;
}

#user-propic {
  width: 45px;
  height: 45px;
  border-top-left-radius: 40%;
    border-bottom-right-radius: 50%;
  box-shadow: 0 0 8px #0004;
}
</style>
