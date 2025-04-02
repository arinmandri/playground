<script setup lang="ts">
import { onMounted } from "vue";

onMounted(async () => {
});
</script>

<template>
  <div>
    <nav>
      <router-link to="/">홈</router-link> |
      <router-link to="/board">게시판</router-link>
    </nav>

    <router-view />
  </div>
</template>

<style scoped>
nav {
  margin-bottom: 20px;
}
</style>
