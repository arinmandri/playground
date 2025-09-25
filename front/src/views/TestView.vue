<template>
  <!-- TODO admin -->
  <h1>TEST</h1>
  <div>
    <h2>파일 여러개 업로드</h2>
    <input type="file" id="file1" accept="image/*" />
    <input type="file" id="file2" accept="image/*" />
    <input type="file" id="file3" accept="image/*" />
    <button @click="gogo">GO</button>
  </div>
</template>

<script setup lang="ts">

import api from '@/api/api';


function gogo() {
  const response = api.uploadFiles([
    (document.getElementById('file1') as HTMLInputElement).files?.[0] as File,
    (document.getElementById('file2') as HTMLInputElement).files?.[0] as File,
    (document.getElementById('file3') as HTMLInputElement).files?.[0] as File,
  ]);
  console.log(response);
}

</script>
