<template>
  <div id="user">
    <div v-if="isLoggedIn">
      <router-link to="/member/myplace">
        <span>{{ user.nick }} 님 입장</span>
        <img class="propic" v-if="user.propic" :src="user.propic">
      </router-link>
    </div>
    <div v-else>
      <span><router-link to="/member/login">손님 입장하세요</router-link></span>
    </div>
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
#user {
  padding: 10px;
  text-align: right;
}

#user .propic {
  width: 45px;
  height: 45px;
  border-top-left-radius: 40%;
  border-bottom-right-radius: 50%;
  box-shadow: 0 0 8px #0004;
}
</style>
