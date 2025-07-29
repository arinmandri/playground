<template>
    <div class="join-view">
        <h2>회원 가입</h2>
        <form @submit.prevent="onSubmit">
            <div>
                <label for="keyname">로그인용 ID<span class="mandatoryMark">*</span></label>
                <input
                    id="keyname"
                    v-model="form.keyname"
                    type="text"
                />
                <span v-if="keynameError" class="error">{{ keynameError }}</span>
            </div>
            <div>
                <label for="password">비밀번호<span class="mandatoryMark">*</span></label>
                <input
                    id="password"
                    v-model="form.password"
                    type="password"
                />
            </div>
            <div>
                <label for="passwordConfirm">비밀번호 확인<span class="mandatoryMark">*</span></label>
                <input
                    id="passwordConfirm"
                    v-model="form.passwordConfirm"
                    type="password"
                />
            </div>
            <div>
                <label for="email">Email</label>
                <input
                    id="email"
                    v-model="form.email"
                    type="email"
                />
            </div>
            <div>
                <label for="nickname">별명<span class="mandatoryMark">*</span></label>
                <input
                    id="nickname"
                    v-model="form.nickname"
                    type="text"
                />
            </div>
            <button type="submit" :disabled="loading">Sign Up</button>
            <div v-if="error" class="error">{{ error }}</div>
            <div v-if="success" class="success">Sign up successful!</div>
        </form>
    </div>
</template>

<script lang="ts" setup>
import api from "@/api/axiosInstance";

import { ref } from 'vue'
import { useRouter } from 'vue-router';

const router = useRouter();

interface FormData {
    keyname: string
    password: string
    passwordConfirm: string
    email: string
    nickname: string
}

const form = ref<FormData>({
    keyname: '',
    password: '',
    passwordConfirm: '',
    email: '',
    nickname: ''
})

const loading = ref(false)
const error = ref('')
const success = ref(false)
const keynameError = ref('')

async function onSubmit() {
    error.value = ''
    success.value = false
    keynameError.value = ''
    loading.value = true

    //// 필수입력항목 확인
    const formValidationMsg = isFormValid();
    if (formValidationMsg !=null) {
        error.value = formValidationMsg;
        loading.value = false
        return
    }

    //// 비밀번호 확인
    if (!confirmPassword()) {
        error.value = 'Passwords do not match.'
        loading.value = false
        return
    }

    //// 키네임 중복 확인
    const isDuplicateKeyname = await checkKeynameDuplicate(form.value.keyname)
    if (isDuplicateKeyname) {
        keynameError.value = 'Keyname already exists.'
        loading.value = false
        return
    }

    try {
        await requestJoin();
        success.value = true
        router.push('/')
    } catch (e: any) {
        error.value = e.response?.data?.message || 'Sign up failed.'
    } finally {
        loading.value = false
    }
}

//// 회원가입 요청
async function requestJoin() {
    const response = await api.post("/member/add/basic", {
        member: {
            nick: form.value.nickname,
            email: form.value.email
        },
        key: {
            keyname: form.value.keyname,
            password: form.value.password
        }
    });
    return response.data;
}

//// 키네임 중복 확인
async function checkKeynameDuplicate(keyname: string): Promise<boolean> {
    console.log('keyname need to be checked not duplicate:', keyname);
    // TODO
    return false
}

//// 필수 필드가 모두 채워졌는지 확인
function isFormValid(): string | null {
    if (!form.value.keyname) return '로그인용 ID를 입력하세요.';
    if (!form.value.password) return '비밀번호를 입력하세요.';
    if (!form.value.nickname) return '별명을 입력하세요.';
    return null;
}

//// 비밀번호 확인
function confirmPassword(): boolean {
  return form.value.password === form.value.passwordConfirm;
}
</script>

<style scoped>
.mandatoryMark {
    color: red;
}
.join-view {
    max-width: 400px;
    margin: 2rem auto;
    padding: 2rem;
    border: 1px solid #ccc;
    border-radius: 8px;
}
.error {
    color: red;
    margin-top: 0.5rem;
}
.success {
    color: green;
    margin-top: 0.5rem;
}
form > div {
    margin-bottom: 1rem;
}
label {
    display: block;
    margin-bottom: 0.25rem;
}
input {
    width: 100%;
    padding: 0.5rem;
    box-sizing: border-box;
}
button {
    padding: 0.5rem 1rem;
}
</style>