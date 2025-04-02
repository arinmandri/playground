import express from "express";
import cors from "cors";

const app = express();
const port = 3000;

app.use(cors());

const boardData = [
  { id: 1, title: "첫 번째 글", author: "사용자1", date: "2025-04-01" },
  { id: 2, title: "두 번째 글", author: "사용자2", date: "2025-04-02" },
  { id: 3, title: "세 번째 글", author: "사용자3", date: "2025-04-03" },
];

app.get("/api/board", (req, res) => {
  res.json(boardData);
});

app.listen(port, () => {
  console.log(`서버가 http://localhost:${port} 에서 실행 중`);
});
