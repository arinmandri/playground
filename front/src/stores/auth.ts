import axios from 'axios';
import { defineStore } from "pinia";

const API_BASE = import.meta.env.VITE_API_BASE_URL;
const USER_TYPE_GUEST = 'guest';
const USER_TYPE_NORMAL = 'normal';

//// 인증 관련 API만 axiosInstance를 통하지 않고 별개로
const ax = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*"
  },
  timeout: 5000,
});

ax.interceptors.request.use((config) => {
  let token = getAccessToken();
  config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const useAuthStore = defineStore("auth", {
  state: () => ({
    isLoggedIn: !!getRefreshToken(),// 로그인 상태 기준: 리프레시토큰 있으면 로그인 상태
    user: {
      type: USER_TYPE_GUEST,
      nick: '…',
      propic: null as string | null,
    },
  }),
  actions: {
    loadAuthInfoFromLocal() {
      /*
       * 액세스토큰 있으면 whoami 하고
       * 없으면 손님로그인
       */
      let access_token = getAccessToken();
      if (access_token == null) {
        this.loginAsGuest();
      } else {
        ax.get(`${API_BASE}/member/whoami`)
          .then((res) => {
            this.user = res.data;
          }).catch((err) => {
            this.reissueToken();
          });
      }
    },
    async loginAsGuest() {
      const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/guest`)).data;
      setTokens(access_token, refresh_token);
      this.whoami();
      this.isLoggedIn = false;
    },
    async loginAsAdmin(keyname: string, password: string) {
      const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/admember`, { keyname, password })).data;
      setTokens(access_token, refresh_token);
      this.whoami();
      this.isLoggedIn = true;
    },
    async loginWithBasicKey(keyname: string, password: string) {
      const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/basic`, { keyname, password })).data;
      setTokens(access_token, refresh_token);
      this.whoami();
      this.isLoggedIn = true;
    },
    async reissueToken(): Promise<void> {// XXX 그냥 비회원도 리프레시 되게 하나? 사실 비회원에 대해 뭐 처리하는 건 없어서 내가 또 쓸데없는 짓 생각하나 싶기도 하고.
      try {
        if (this.isLoggedIn) {// 회원
          console.log('회원 토큰 발급 시도');
          await this.refreshToken();
        }
        else {// 비회원
          console.log('비회원 토큰 발급 시도');
          await this.loginAsGuest();
        }
      } catch (e: any) {
        console.log('토큰 재발급 실패; 비회원 토큰 발급 시도');
        await this.loginAsGuest();
      }
    },
    async refreshToken() {
      const refresh_token_current = getRefreshToken();
      const { access_token, refresh_token } = (await axios.post(`${API_BASE}/auth/token/refresh`, { refreshToken: refresh_token_current })).data;
      setTokens(access_token, refresh_token);
      this.whoami();
    },
    logout() {
      clearTokens();
      this.loginAsGuest();
      console.log('로그아웃');
    },
    whoami() {
      ax.get(`${API_BASE}/member/whoami`)
        .then((res) => {
          this.user = res.data;
        })
    },
  }
});

const ACCESS_TOKEN_KEY = 'access_token';
const REFRESH_TOKEN_KEY = 'refresh_token';

export async function getEnsuredAccessToken() {
  if (!getAccessToken()) {
    const authStore = useAuthStore();
    await authStore.loginAsGuest();
  }
  return getAccessToken();
}

function getAccessToken() {
  return localStorage.getItem(ACCESS_TOKEN_KEY);
}

function getRefreshToken(): string | null {
  return localStorage.getItem(REFRESH_TOKEN_KEY);
}

function setTokens(access: string, refresh: string) {
  localStorage.setItem(ACCESS_TOKEN_KEY, access == null ? '' : access);
  localStorage.setItem(REFRESH_TOKEN_KEY, refresh == null ? '' : refresh);
  console.log('토큰 저장', access?.slice(-4), refresh?.slice(0, 4));
}

function clearTokens() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
  console.log('토큰 삭제');
}
