// src/api/api.ts
import axios from "axios";
import { getAccessToken, getRefreshToken, setTokens, clearTokens } from "./TokenService";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "/api",
});

// Request interceptor: gắn accessToken
api.interceptors.request.use(
  (config) => {
    const token = getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor: xử lý khi 401 (token hết hạn)
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      const refreshToken = getRefreshToken();
      if (refreshToken) {
        try {
          const res = await axios.post("/v1/auth/refresh", { refreshToken });
          const newAccessToken = res.data.newAccessToken;

          // Lưu lại token mới
          setTokens(newAccessToken);

          // Gọi lại request cũ với token mới
          error.config.headers.Authorization = `Bearer ${newAccessToken}`;
          return api(error.config);
        } catch {
          clearTokens();
          window.location.href = "/login";
        }
      } else {
        clearTokens();
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default api;
