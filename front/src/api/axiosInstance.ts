import { useAuthStore, ensureToken, getAccessToken, getRefreshToken } from '@/stores/auth';

import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*"
    },
    timeout: 5000,
});

let isRefreshing = false;
let pendingRequests: (() => void)[] = [];

api.interceptors.request.use(async (config) => {
    await ensureToken();
    const token = getAccessToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;
        const status = error.response?.status;

        if (status === 403 && !originalRequest._retry) {// 응답이 403일 때 1회에 한해 토큰 리프레시 후 재시도
            originalRequest._retry = true;// 이 API 요청이 재시도인지 표시

            if (!isRefreshing) {// 토큰 리프레시 중복 방지 XXX 그러나 여러 탭이 열림을 생각하면 경합을 완전히 예방하진 못한다. 하지만 일단 이 정도로 넘어감.
                // XXX 리프레시 토큰 사용 결과가 403인 경우의 처리 없음.
                const authStore = useAuthStore();
                isRefreshing = true;
                try {
                    const refresh_token_curr = getRefreshToken();
                    //// 토큰 재발급
                    if (!refresh_token_curr) {// 리프레시토큰 없음: 비회원
                        console.log('비회원 토큰 발급 시도');
                        await authStore.loginAsGuest();
                    } else {// 회원
                        console.log('회원 토큰 발급 시도');
                        await authStore.refreshToken(refresh_token_curr);
                    }

                    //// 대기요청큐 실행
                    pendingRequests.forEach((cb) => cb());
                    pendingRequests = [];
                } catch (err) {
                    console.log('토큰 리프레시 실패');
                    authStore.logout();
                    await authStore.loginAsGuest();
                    return Promise.reject(err);
                } finally {
                    isRefreshing = false;
                }
            }

            // 대기 중인 요청 큐에 등록
            return new Promise((resolve) => {
                pendingRequests.push(() => {
                    originalRequest.headers.Authorization = `Bearer ${getAccessToken()}`;
                    resolve(api(originalRequest));
                });
            });
        }

        return Promise.reject(error);
    }
);

export default api;
