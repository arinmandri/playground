import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/HomeView.vue";
import Board from "../views/BoardView.vue";
import MemberJoin from "../views/member/JoinView.vue";
import MemberLogin from "../views/member/LoginView.vue";
import { useAuthStore } from "@/stores/auth";

const routes = [
  { name: "home", path: "/", component: Home },
  { name: "board", path: "/board", component: Board },
  { name: "memberJoin", path: "/member/join", component: MemberJoin },
  { name: "memberLogin", path: "/member/login", component: MemberLogin },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 전역 가드
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  //// 이미 로그인 상태이면 홈으로
  if (to.name === "memberLogin" && authStore.isAuthenticated) {
    return next({ name: "home" });
  }

  return next();
});

export default router;
