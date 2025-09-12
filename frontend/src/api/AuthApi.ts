// src/api/AuthApi.ts
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "/api",
});

export type RegisterRequest = {
  email: string;
  password: string;
  roleIds?: number[];
};

export const registerUser = (data: RegisterRequest) =>
  api.post("/v1/auth/register", {
    ...data,
    roleIds: data.roleIds || [1], // mặc định roleIds = [1]
  });

// Login
export const loginUser = async (data: { email: string; password: string }) => {
  const response = await api.post("/v1/auth/login", data);

    // Lấy token đúng chỗ
    const { accessToken, refreshToken } = response.data.data;
    console.log(accessToken)

  // Nếu login thành công -> lưu token
  if (accessToken) {
    localStorage.setItem("accessToken", accessToken);
    if (refreshToken) {
      localStorage.setItem("refreshToken", refreshToken);
    }
  }

  return response;
};

// Logout (xóa token khỏi localStorage)
export const logoutUser = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
};
