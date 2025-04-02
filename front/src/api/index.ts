import axios from "axios";

const base_url = import.meta.env.VITE_API_BASE_URL;

const apiClient = axios.create({
  baseURL: base_url,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 5000,
});

export default apiClient;
