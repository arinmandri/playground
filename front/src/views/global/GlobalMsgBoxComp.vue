<template>
  <ul id="globalMsgBox" v-if="msgStore.msgs.length > 0">
    <li v-for="msg in msgStore.msgs" :key="msg.id" :class="`globalmsg globalmsg-${msg.msgClass}`">
      {{ msg.content }}
      <button class="closeBtn" @click="msgStore.removeMsg(msg.id)">✕</button>
    </li>
  </ul>
</template>

<script setup lang="ts">

import { useMsgStore } from '@/stores/globalMsg'; const msgStore = useMsgStore();

</script>

<style>
#globalMsgBox {
  position: sticky;
  top: 10px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  z-index: 9999;
  list-style: none;
}

#globalMsgBox .closeBtn {
  position: absolute;
  top: 0px;
  right: 0px;
  padding-right: 6px;
  border: none;
  background: transparent;
  font-size: 120%;
  font-weight: bold;
  color: inherit;
  cursor: pointer;
}

#globalMsgBox .globalmsg {
  position: relative;
  padding: 12px 16px;
  border: 2px solid;
  border-top-width: 5px;
  border-radius: 3px;
  background: #fff;
  box-shadow: 0 2px 6px #0003;
  animation:
    borderFade 2.8s linear 4s forwards,
    fadeOut 0.2s ease 6.8s forwards;
}

#globalMsgBox .globalmsg-error {
  border-color: #d20;
  color: #d20;
  background: #ffe5e5;
}

#globalMsgBox .globalmsg-warn {
  border-color: #c80;
  color: #c80;
  background: #fff4e0;
}

#globalMsgBox .globalmsg-info {
  border-color: #06e;
  color: #06e;
  background: #e6f2ff;
}

#globalMsgBox .globalmsg-debug {
  border-color: #084;
  color: #084;
  background: #e5ffe5;
}

@keyframes borderFade {
  from {
    border-color: currentColor;
  }

  to {
    border-color: transparent;
  }
}

@keyframes fadeOut {
  from {
    opacity: 1;
    transform: translateX(0);
  }

  to {
    opacity: 0;
    transform: translateX(20px);
  }
}
</style>
