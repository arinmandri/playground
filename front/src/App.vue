<template>
  <div>
    <nav>
      <ul id="globalMenu">
        <li><router-link to="/">홈</router-link></li>
        <li><router-link to="/board">게시판</router-link></li>
      </ul>
      <div id="user">
        <div v-if="isLoggedIn">
          <router-link to="/member/myplace">(회원닉네임)</router-link><!-- TODO -->
        </div>
        <div v-else>
          <span><router-link to="/member/login">로그인</router-link></span>
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
import { storeToRefs } from "pinia";

const { isLoggedIn } = storeToRefs(authStore);

onMounted(async () => {
});
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

button,
input,
select,
textarea {
  border: 1px solid grey;
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
  box-shadow: 0px 2px 8px rgba(0, 0, 0, 0.3);
}
#routerViewDiv>* {
  margin: 0 20px;
}
</style>
