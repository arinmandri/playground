/* eslint-disable @typescript-eslint/no-explicit-any */
/**
 * API 호출용 모듈.
 * 
 * 모든 API 요청은 이 모듈을을 이용해서 한다.
 * 
 * 모든 요청은 액세스토큰을 가지고 한다.
 * 액세스토큰 발급용 API들만은 예외이다.
 * 인터셉터를 이용하여 액세스토큰 삽입.
 * 요청을 보내려 할 때 액세스 토큰이 없으면 액세스토큰 얻기 기능을 먼저 실행 후 요청을 마저 보낸다.
 * 
 * 로그인하여 회원용 액세스토큰을 얻는다.
 * 비로그인 상태인 경우 비회원용 액세스토큰을 얻는다.
 * 
 * 요청이 실패한 경우 1회 재시도한다.
 * 응답이 401, 403 상태가 아닌 경우 요청을 단순히 반복한다.
 * 
 * 응답이 401, 403 상태인 경우; 토큰을 갱신한 후 재시도한다.
 * 단; 동시에 여러 요청의 응답이 401, 403 상태인 경우 토큰 갱신을 한 번만 하기 위해 다음과 같이 실행한다.
 * 재시도 대기큐가 있다.
 * 토큰갱신중 상태가 아닌 경우 토큰을 갱신(이 동안 '토큰갱신중 상태'로 표시한다)한 후 대기큐의 모든 요청을 재시도한다.
 * 토큰갱신중 상태인 경우 대기큐로 들어간다.
 */

import { useAuthStore, getEnsuredAccessToken } from '@/stores/auth';

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
  let token = await getEnsuredAccessToken();
  config.headers.Authorization = `Bearer ${token}`;
  return config;
});

let isRefreshing = false;
let retryQueue: (() => void)[] = [];// 401, 403 재시도 대기큐.

const api = {
  get: (url: string, params = {}): Promise<AxiosResponse<any>> => {
    return new Promise((resolve, reject) => {
      const attemptRequest = attemptRequestOf(ax.get, url, { params }, resolve, reject);
      attemptRequest();
    });
  },
  post: async (url: string, data?: any): Promise<AxiosResponse<any, any>> => {
    return new Promise((resolve, reject) => {
      const attemptRequest = attemptRequestOf(ax.post, url, data, resolve, reject);
      attemptRequest();
    });
  }
}

const RETRY_MAX = 3;

function attemptRequestOf(fun: (url: string, params: any) => any, url: string, params: any, resolve: any, reject: any) {
  const attemptRequest = (retryCount = 0) => {
    if (retryCount > 0) console.log(url, '재시도: ' + retryCount);
    fun(url, params)
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


