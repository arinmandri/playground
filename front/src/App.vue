<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";

const boardList = ref([]);

onMounted(async () => {
  try {
    const response = await axios.get("http://localhost:3000/api/board");
    boardList.value = response.data;
  } catch (error) {
    console.error("데이터를 불러오는데 실패했습니다.", error);
  }
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
