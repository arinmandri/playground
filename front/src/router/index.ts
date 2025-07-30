import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/HomeView.vue";
import Board from "../views/board/BoardView.vue";
import MemberJoin from "../views/member/JoinView.vue";
import MemberLogin from "../views/member/LoginView.vue";
import BoardPostWrite from "../views/board/PostWriteView.vue";
import { useAuthStore } from "@/stores/auth";

const routes = [
  { name: "home", path: "/", component: Home },
  { name: "board", path: "/board", component: Board },
  { name: "memberJoin", path: "/member/join", component: MemberJoin },
  { name: "memberLogin", path: "/member/login", component: MemberLogin },
  { name: "boardPostWrite", path: "/board/post/write", component: BoardPostWrite },
];

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
