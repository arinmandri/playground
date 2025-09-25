<template>
	<form @submit.prevent="submitPost">
		<textarea v-model="formData.content" type="text">뭐 쓸라고 했더라</textarea>
		<PAttachmentListForm ref="pAttachmentListForm" :title="'첨부파일'"
			v-model:attachments="(formData.attachments as PAttachment[])" :maxLength="5" />
		<button type="submit">라고 쓰기</button>
	</form>
</template>

<script lang="ts" setup>

import PAttachmentListForm from '@/views/board/post/comp/PAttachmentListForm.vue';

import { type PostWrite, PAttachment } from "@/views/board/services/types";
import { type Z_PostAdd, toApiSendingFormOfAtt } from "@/api/board";

import { ref, type Ref, defineExpose } from 'vue'

const emit = defineEmits<{
	(e: 'submit', exportProps: Z_PostAdd): void;
}>();

const formData = ref<PostWrite>({
	content: '',
	attachments: [],
}) as Ref<PostWrite>;

interface PAttachmentListForm_intf {
	uploadFiles: () => Promise<void>
}
const pAttachmentListForm = ref<PAttachmentListForm_intf>() as Ref<PAttachmentListForm_intf>;


async function submitPost() {
	await pAttachmentListForm.value.uploadFiles();
	const attsToSend = formData.value.attachments.map(attRaw => toApiSendingFormOfAtt(attRaw));
	emit('submit', {
		content: formData.value.content,
		attachments: attsToSend,
	});
	formData.value.content = ''
	formData.value.attachments = [];
}

// TODO loading?
</script>
