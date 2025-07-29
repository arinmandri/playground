import axios from 'axios';
import { defineStore } from "pinia";

const API_BASE = import.meta.env.VITE_API_BASE_URL;

export const useAuthStore = defineStore("auth", {
    state: () => ({
        isAuthenticated: getRefreshToken() != null,
    }),
    actions: {
        async loginAsGuest() {
            const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/guest`)).data;
            setTokens(access_token, refresh_token);
        },
        async loginWithBasicKey(keyname: string, password: string) {
            const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/basic`, { keyname, password })).data;
            setTokens(access_token, refresh_token);
            this.isAuthenticated = true;
        },
        async refreshToken(refreshToken: string) {
            const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/refresh`, { refreshToken })).data;
            setTokens(access_token, refresh_token);
        },
        logout() {
            clearTokens();
            this.isAuthenticated = false;
            console.log('로그아웃');
        },
    }
});

export async function ensureToken() {
    if (!getAccessToken()) {
        const authStore = useAuthStore();
        await authStore.loginAsGuest();
    }
}

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

export function getAccessToken() {
    return localStorage.getItem(ACCESS_TOKEN_KEY);
}

export function getRefreshToken() {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
}

function setTokens(access: string, refresh: string) {
    if (access != null)
        localStorage.setItem(ACCESS_TOKEN_KEY, access);
    if (refresh != null)
        localStorage.setItem(REFRESH_TOKEN_KEY, refresh);
    console.log('토큰 저장', access, refresh);
}

// TODO 로그아웃으로 대체
function clearTokens() {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    console.log('토큰 삭제');
}
