<template>
  <GlobalNaviComp />

  <div>
    <GlobalMsgBoxComp />

    <GlobalUserinfoComp />

    <div id="routerViewDiv">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">

import GlobalNaviComp from './views/global/GlobalNaviComp.vue';
import GlobalMsgBoxComp from '@/views/global/GlobalMsgBoxComp.vue';
import GlobalUserinfoComp from '@/views/global/GlobalUserinfoComp.vue';

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
  word-break: break-word;
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

#routerViewDiv {
  margin: 8px auto 20px auto;
  padding: 20px 0;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.3);
  background-color: #f6f6f6;
}

#routerViewDiv>* {
  margin: 0 20px;
}
</style>
