import express from "express";
import cors from "cors";
import { getBoardList } from './db/db.js'

const app = express();
const port = 3000;

app.use(cors());

app.get("/board", async (req, res) => {
  const list = await getBoardList();
  res.json(list);
});

app.listen(port, () => {
  console.log(`서버 실행중 (${port} 포트)`);
});
