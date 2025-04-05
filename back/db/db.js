import pg from 'pg';
const { Pool } = pg;

const pool = new Pool({
    host: "localhost",
    database: "arinmandri",
    user: "playground-backend",
    password: "bungchi^^patchi",
    port: 5432,
});

export async function getBoardList() {
    const query = 'SELECT id, title, author, created_at FROM "playground"."post" ORDER BY id DESC';
    const result = await pool.query(query);
    return result.rows;
}
