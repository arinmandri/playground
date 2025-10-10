<template>
  <div>
    없는 페이지입니다. 주소를 다시 확인하세요.
    <div>
      <button @click="router.back()">이전 페이지로</button>
      <router-link to="/">홈으로</router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

</script>