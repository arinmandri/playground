import { createRouter, createWebHistory } from "vue-router";

import { useAuthStore } from "@/stores/auth";

/*
 * 이름이 "View.vue"로 끝나는 모든 컴포넌트를 자동으로 라우트로 등록
 */
const vueFiles = import.meta.glob("@/views/**/*View.vue", { eager: true });
const routes = Object.keys(vueFiles).map((filePath) => {
  const temp = filePath
      .replace('/src/views/', '')// 상대경로로
      .replace(/View\.vue$/, '')// "View.vue" 제거
      .replace(/\/$/, '')// "/" 제거 (파일명이 View.vue 인 경우)
      .toLowerCase();
  const name = temp === '' ? 'home' : temp.replace(/\//g, '-');
  const path = '/' + temp;
  return {
    name,
    path,
    component: (vueFiles as any)[filePath].default,
  };
});

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 전역 가드
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  if (authStore.isLoggedIn) {// 로그인 상태
    if (to.name === "memberLogin") {// 로그인페이지 --> 이전 페이지 | 홈
      if (window.history.length > 1) {
        return next(from.fullPath);
      } else {
        return next({ name: "home" });// 이전 페이지 없음 --> 홈
      }
    }
  } else { }

  return next();
});

export default router;
