import { createRouter, createWebHistory } from "vue-router";
import Home from "../views/HomeView.vue";
import Board from "../views/BoardView.vue";

const routes = [
  { path: "/", component: Home },
  { path: "/board", component: Board },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
