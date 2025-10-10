<template>
  <div id="globalMsgArea">
    <div id="globalMsgSettings" :class="isDev ? 'dev' : ''">
      Msg level:
      <label><input type="radio" value="error" v-model="level" /> Error</label>
      <label><input type="radio" value="warn" v-model="level" /> Warn</label>
      <label><input type="radio" value="info" v-model="level" /> Info</label>
      <label><input type="radio" value="debug" v-model="level" /> Debug</label>
    </div>
    <ul id="globalMsgBox" v-if="filteredMsgs.length > 0">
      <li v-for="msg in filteredMsgs" :key="msg.id"
        :class="[`globalmsg globalmsg-${msg.msgClass}`, { fixed: fixedSet.has(msg.id) }]" @click="handleClick(msg.id)">
        {{ msg.content }}
        <button class="closeBtn" @click="msgStore.removeMsg(msg.id)">✕</button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import { useMsgStore } from '@/stores/globalMsg';
const msgStore = useMsgStore();

import { ref, computed, type Ref } from 'vue';

const isDev = import.meta.env.DEV;
const level = ref<'error' | 'warn' | 'info' | 'debug'>(isDev ? 'debug' : 'warn');
const fixedSet = ref<Set<number>>(new Set()) as Ref<Set<number>>;// 고정된 메시지들
const levelOrder = ['error', 'warn', 'info', 'debug'];
const filteredMsgs = computed(() => {
  const idx = levelOrder.indexOf(level.value);
  return msgStore.msgs.filter(msg => {
    const msgIdx = levelOrder.indexOf(msg.msgClass);
    return msgIdx <= idx;
  });
});

function handleClick(msg_id: number) {
  msgStore.disableAutoRemove(msg_id);
  fixedSet.value.add(msg_id);
}
</script>

<style>
#globalMsgArea {
  position: sticky;
  top: 0px;
  padding: 8px;
}

#globalMsgSettings {
  display: none;
}

#globalMsgSettings.dev {
  display: flex;
  align-items: right;
  justify-content: flex-end;
  gap: 10px;
}

#globalMsgBox {
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
}

#globalMsgBox .globalmsg.fixed {
  animation:
    borderFade 0s linear 0s forwards;
}

#globalMsgBox .globalmsg:not(.fixed) {
  animation:
    borderFade 2.8s linear 4s forwards,
    fadeOut 0.2s ease 6.8s forwards;
}

#globalMsgBox .globalmsg-error {
  border-color: #d05;
  color: #d05;
  background: #ffe5e5;
}

#globalMsgBox .globalmsg-warn {
  border-color: #b80;
  color: #b80;
  background: #fff4e0;
}

#globalMsgBox .globalmsg-info {
  border-color: #09d;
  color: #09d;
  background: #e6f2ff;
}

#globalMsgBox .globalmsg-debug {
  border-color: #666;
  color: #666;
  background: #fff;
  font-family: monospace;
  white-space: pre-wrap;
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
