import { createRouter, createWebHistory } from "vue-router";

import { useAuthStore } from "@/stores/auth";

import NotFound from '@/views/NotFound.vue';

// TODO 더 구체적으로 권한을 따져야

//// 인증이 필요한 라우트 목록
const authRequiredRoutes = [
  '/board/post/write',
  '/member/myplace',
];

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
  const requiresAuth = authRequiredRoutes.includes(path);
  console.log(`★router - register: ${name} --> ${path}`);

  return {
    name,
    path,
    component: (vueFiles as any)[filePath].default,
    meta: { requiresAuth }
  };
});
routes.push(// 없는 페이지
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: NotFound, meta: {
      requiresAuth: false
    },
  }
);

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 전역 가드
router.beforeEach((to, from) => {

  // TEST
  console.log(`★router: ${from.fullPath} --> ${to.fullPath}`);

  const authStore = useAuthStore();

  if (authStore.isLoggedIn) {// 로그인 상태
    if (to.name === "memberLogin") {// 로그인페이지 --> 이전 페이지 or 홈
      if (window.history.length > 1) {
        return { path: from.fullPath };
      } else {
        return { name: "home" };// 이전 페이지 없음 --> 홈
      }
    }
  } else {// 비로그인 상태
    if (to.meta.requiresAuth) {// 로그인이 필요한 페이지
      return { name: 'member-login', query: { redirect: to.fullPath } };
    }
  }
});

export default router;
