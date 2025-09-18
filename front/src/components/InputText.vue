<!--
form 속에 텍스트필드 1개.


사용 예시
import InputText from '@/components/InputText.vue';
const form = ref({nick: ''})
<InputText :title="'별명'" v-model:textValue="form.nick" :isRequired="true" />
-->
<template>
  <div class="inputBox input-text">
    <p v-if="props.title" class="inputTitle">{{ props.title }}</p>
    <label>
      <input type="text" :value="props.textValue" @input="onValueChange" :required="props.isRequired" />
    </label>
  </div>
</template>

<script setup lang="ts">

const props = defineProps<{
  title?: string;
  textValue: string;
  isRequired: boolean;
}>();

const emit = defineEmits<{
  (e: "update:textValue", exportProps: string): void;
}>();

function onValueChange(event: Event) {
  const value = (event.target as HTMLInputElement).value.trim();
  emit("update:textValue", value);
}

</script>

<style scoped>
.inputTitle {
  font-weight: bold;
  font-size: 80%;
}
</style>
