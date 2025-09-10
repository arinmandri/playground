<template>
  <div>
    없는 페이지입니다. 주소를 다시 확인하세요.
    <button @click="goBack">이전 페이지로</button>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';

const router = useRouter();

function goBack() {
  router.back();
}
</script>