<template>
  글 수정 페이지 TODO {{ post }}
</template>

<script setup lang="ts">

import { useRoute } from 'vue-router';

const route = useRoute();
const post = route.params.post;

</script>
