<script setup lang="ts">
import { onMounted } from "vue";

onMounted(async () => {
});
</script>

<template>
  <div>
    <nav>
      <router-link to="/">홈</router-link> |
      <router-link to="/board">게시판</router-link> |
      <router-link to="/member/join">가입</router-link> |
      <router-link to="/member/login">로그인</router-link> |
    </nav>

    <router-view />
  </div>
</template>

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
</style>
