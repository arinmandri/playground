import axios from 'axios';
import { getAccessToken, getRefreshToken, setTokens, clearTokens } from './tokenStorage';

const API_BASE = import.meta.env.VITE_API_BASE_URL;

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

async function ensureToken() {
    if (!getAccessToken()) {
        const { access_token, refresh_token } = await getGuestToken();
        setTokens(access_token, refresh_token);
    }
}

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
                isRefreshing = true;
                try {
                    const refresh_token_curr = getRefreshToken();
                    if (!refresh_token_curr) throw new Error('No refresh token');

                    //// 토큰 재발급
                    const { access_token, refresh_token } = await refreshToken(refresh_token_curr);
                    setTokens(access_token, refresh_token);

                    //// 대기요청큐 실행
                    pendingRequests.forEach((cb) => cb());
                    pendingRequests = [];
                } catch (err) {
                    clearTokens();
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

export async function getGuestToken() {
    const res = await axios.post(`${API_BASE}/auth/token/guest`);
    return res.data;
}

export async function getBasicToken(keyname: string, password: string) {
    const res = await axios.post(`${API_BASE}/auth/token/basic`, { keyname, password });
    return res.data;
}

export async function refreshToken(refreshToken: string) {
    const res = await axios.post(`${API_BASE}/auth/token/refresh`, { refreshToken });
    return res.data;
}

export default api;
