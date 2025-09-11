/* eslint-disable @typescript-eslint/no-explicit-any */
/**
 * axiosInstance.ts의 복붙이다. 만약 앞으로 axiosInstance.ts를 또 복붙해야 할 일이 있다면 그 때 가서 공통부분 추출함.
 * axiosInstance.ts와의 차이점
 * - ADMIN_API_BASE_URL
 * - api에 get과 post 이외의 메서드 존재.
 * 지금은 사용자 백엔드와 관리자 백엔드가 같은 앱에 있는데(이 프론트엔드도 그렇고) 원래 다르게 할 생각이 있었기 때문에... 그냥 혹시나 해서 BASE_URL을 다르게 하도록 api 개체도 분리한 것이다.
 */

import { useAuthStore, getEnsuredAccessToken } from '@/stores/auth';

import axios from 'axios';
import type { AxiosResponse } from 'axios';

const URL_FILE_ADD = '/file/add';
const ADMIN_API_BASE_URL = import.meta.env.VITE_ADMIN_API_BASE_URL;

const ax = axios.create({
  baseURL: ADMIN_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*"
  },
  timeout: 5000,
});

ax.interceptors.request.use(async (config) => {
  let token = await getEnsuredAccessToken();
  config.headers.Authorization = `Bearer ${token}`;
  return config;
});

let isRefreshing = false;
let retryQueue: (() => void)[] = [];// 401, 403 재시도 대기큐.

const api = {
  get: (url: string, params = {}): Promise<AxiosResponse<any>> => {
    return new Promise((resolve, reject) => {
      const attemptRequest = attemptRequestOf(resolve, reject, ax.get, url, { params });
      attemptRequest();
    });
  },
  post: async (url: string, data?: any): Promise<AxiosResponse<any, any>> => {
    return new Promise((resolve, reject) => {
      const attemptRequest = attemptRequestOf(resolve, reject, ax.post, url, data);
      attemptRequest();
    });
  },
  put: async (url: string, data?: any): Promise<AxiosResponse<any, any>> => {
    return new Promise((resolve, reject) => {
      const attemptRequest = attemptRequestOf(resolve, reject, ax.put, url, data);
      attemptRequest();
    });
  },
  postFormData: async (formData: FormData): Promise<AxiosResponse<any, any>> => {
    return new Promise((resolve, reject) => {
      const attemptRequest = attemptRequestOf(resolve, reject, ax.post, URL_FILE_ADD, formData,
        {
          headers: { 'Content-Type': 'multipart/form-data' }
        }
      );
      attemptRequest();
    });
  },
}

const RETRY_MAX = 3;

function attemptRequestOf(
  resolve: any,
  reject: any,
  axAction: (url: string, data: any, options: any) => any,
  url: string,
  data: any,
  options?: any
) {
  const attemptRequest = (retryCount = 0) => {
    if (retryCount > 0) console.log(url, '재시도: ' + retryCount);
    axAction(url, data, options)
      .then(resolve)
      .catch(async (error: any) => {
        if (retryCount >= RETRY_MAX) {
          console.error(url, `재시도 횟수(${RETRY_MAX}) 한도 도달`);
          reject(error);
          return;
        }
        const responseStatus = error.response?.status;
        if (responseStatus === 401 || responseStatus == 403) {
          if (isRefreshing) {// 이미 토큰 재발급 중: 큐에 대기
            retryQueue.push(() => {
              attemptRequest(retryCount + 1);
            });
          } else {// 첫 번째 401, 403 요청: 토큰 재발급 후 대기큐의 작업들 실행
            isRefreshing = true;
            try {
              const authStore = useAuthStore();
              await authStore.reissueToken();
              isRefreshing = false;

              // 대기큐에 있는 요청들 모두 재시도
              const queued = retryQueue;
              retryQueue = [];
              queued.forEach(cb => cb());
              attemptRequest(retryCount + 1);// 마지막으로 현재 요청도 재시도

            } catch (refreshError) {
              isRefreshing = false;

              retryQueue.forEach(cb => cb());
              retryQueue = [];

              reject(refreshError);
            }
          }
        } else {// 401, 403 외 에러: 단순 재시도
          attemptRequest(retryCount + 1);
        }
      });
  };

  return attemptRequest;
}

export default api;


