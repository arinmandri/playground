import axios from 'axios';
import { defineStore } from "pinia";

const API_BASE = import.meta.env.VITE_API_BASE_URL;

export const useAuthStore = defineStore("auth", {
    state: () => ({
        isLoggedIn: !!getRefreshToken(),// 로그인 상태 기준: 리프레시토큰 있으면 로그인 상태
    }),
    actions: {
        async loginAsGuest() {
            const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/guest`)).data;
            const { isLoggedIn } = setTokens(access_token, refresh_token);
            this.isLoggedIn = isLoggedIn;
        },
        async loginWithBasicKey(keyname: string, password: string) {
            const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/basic`, { keyname, password })).data;
            const { isLoggedIn } = setTokens(access_token, refresh_token);
            this.isLoggedIn = isLoggedIn;
        },
        async refreshToken(refreshToken: string) {
            const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/refresh`, { refreshToken })).data;
            const { isLoggedIn } = setTokens(access_token, refresh_token);
            this.isLoggedIn = isLoggedIn;
        },
        logout() {
            clearTokens();
            this.isLoggedIn = false;
            console.log('로그아웃');
        },
    }
});

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

export async function ensureToken() {
    if (!localStorage.getItem(ACCESS_TOKEN_KEY)) {
        const authStore = useAuthStore();
        await authStore.loginAsGuest();
    }
}

export async function getAccessToken() {
    await ensureToken();
    return localStorage.getItem(ACCESS_TOKEN_KEY);
}

export function getRefreshToken() {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
}

function setTokens(access: string, refresh: string) {
    localStorage.setItem(ACCESS_TOKEN_KEY, access == null ? '' : access);
    localStorage.setItem(REFRESH_TOKEN_KEY, refresh == null ? '' : refresh);
    console.log('토큰 저장', access?.slice(-4), refresh?.slice(0, 4));
    return { isLoggedIn: !!refresh };
}

function clearTokens() {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    console.log('토큰 삭제');
}
