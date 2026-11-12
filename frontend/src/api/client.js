import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api",
  timeout: 20000,
  headers: { "Content-Type": "application/json" }
});

api.interceptors.request.use(config => {
  const token = sessionStorage.getItem("stockpilot_token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export function getApiError(error) {
  if (error.response) {
    const data = error.response.data;
    if (typeof data === "string") return data;
    return data?.message || data?.error || `Request failed (${error.response.status})`;
  }
  if (error.code === "ECONNABORTED") return "The backend request timed out.";
  return "Cannot connect to Spring Boot. Check that it is running on http://localhost:8080.";
}

export async function checkBackend() {
  const response = await api.get("/health");
  return response.data;
}

export default api;
