/* eslint-disable @typescript-eslint/no-explicit-any */
import { useAuthStore, ensureToken, getAccessToken, getRefreshToken } from '@/stores/auth';

import axios from 'axios';
import type { AxiosResponse } from 'axios';

const ax = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL,
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*"
    },
    timeout: 5000,
});

ax.interceptors.request.use(async (config) => {
    let token = await getAccessToken();
    if (!token) {
        await ensureToken();
        token = await getAccessToken();
    }
    config.headers.Authorization = `Bearer ${token}`;
    console.log(`TOKEN of ${config.url}: `, token?.slice(-4));// TEST
    return config;
});

let isRefreshing = false;
let retryQueue: (() => void)[] = [];// 403 에러 발생 시 대기큐. 여러 요청이 동시에 실패할 경우 요청들이 대기큐에 들어가고, 토큰 재발급을 한 번만 수행 후 대기큐의 요청들을 재시도한다.

const api = {
    get: (url: string, params = {}): Promise<AxiosResponse<any>> => {
        return new Promise((resolve, reject) => {
            const attemptRequest = attemptRequestOf(ax.get, url, { params }, resolve, reject);
            attemptRequest(false);
        });
    },
    post: async (url: string, data?: any): Promise<AxiosResponse<any, any> | void> => {
        return new Promise((resolve, reject) => {
            const attemptRequest = attemptRequestOf(ax.post, url, data, resolve, reject);
            attemptRequest(false);
        });
    }
}

function attemptRequestOf(fun: (url: string, params: any) => any, url: string, params: any, resolve: any, reject: any) {
    const attemptRequest = (isRetry = false) => {
        if (isRetry) console.log(url, '재시도');
        fun(url, params)
            .then(resolve)
            .catch(async (error: any) => {
                if (error.response?.status === 403) {
                    if (isRefreshing) {// 이미 토큰 재발급 중: 큐에 대기
                        retryQueue.push(() => {
                            attemptRequest(true);
                        });
                    } else {// 첫 번째 403 요청: 토큰 재발급 후 대기큐의 작업들 실행
                        isRefreshing = true;
                        try {
                            await refreshToken();
                            isRefreshing = false;

                            // 대기큐에 있는 요청들 모두 재시도
                            const queued = retryQueue;
                            retryQueue = [];
                            queued.forEach(cb => cb());
                            attemptRequest(true);// 마지막으로 현재 요청도 재시도

                        } catch (refreshError) {
                            isRefreshing = false;

                            retryQueue.forEach(cb => cb());
                            retryQueue = [];

                            reject(refreshError);
                        }
                    }
                } else {// 403 외 에러: 단순 재시도
                    const b = fun(url, params)
                        .then((response: any) => response)
                        .catch((error: any) => {
                            console.error('요청 재시도 실패:', error);
                            throw error;
                        });
                    return b;
                }
            });
    };

    return attemptRequest;
}

async function refreshToken(): Promise<void> {
    const authStore = useAuthStore();
    const refresh_token_curr = getRefreshToken();
    try {
        if (!refresh_token_curr) {// 리프레시토큰 없음: 비회원
            console.log('비회원 토큰 발급 시도');
            await authStore.loginAsGuest();
        }
        else {// 회원
            console.log('회원 토큰 발급 시도');
            await authStore.refreshToken(refresh_token_curr);
        }
    } catch (e: any) {
        console.log('토큰 재발급 실패; 비회원 토큰 발급 시도');
        await authStore.loginAsGuest();
    }
}

export default api;


