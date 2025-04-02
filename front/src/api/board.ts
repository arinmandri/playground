import apiClient from "./index";

// 게시판 데이터 가져오기
export async function fetchBoardList() {
    try {
        const response = await apiClient.get("/board");
        return response.data;
    } catch (error) {
        console.error("게시판 데이터를 불러오는데 실패했습니다.", error);
        throw error;
    }
}
