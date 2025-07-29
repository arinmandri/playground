import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/HomeView.vue";
import Board from "../views/BoardView.vue";
import MemberJoin from "../views/member/JoinView.vue";
import MemberLogin from "../views/member/LoginView.vue";

const routes = [
  { path: "/", component: Home },
  { path: "/board", component: Board },
  { path: "/member/join", component: MemberJoin },
  { path: "/member/login", component: MemberLogin },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
