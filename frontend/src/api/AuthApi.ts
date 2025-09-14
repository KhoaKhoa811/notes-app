import api from "./api";
import { setTokens, clearTokens } from "./TokenService";

export type RegisterRequest = {
  email: string;
  password: string;
  roleIds?: number[];
};

// Register
export const registerUser = (data: RegisterRequest) =>
  api.post("/v1/auth/register", {
    ...data,
    roleIds: data.roleIds || [1], // mặc định role = [1]
  });

// Login
export const loginUser = async (data: { email: string; password: string }) => {
  const response = await api.post("/v1/auth/login", data);

  const { accessToken, refreshToken } = response.data.data;

  if (accessToken) {
    setTokens(accessToken, refreshToken);
  }

  return response;
};

// Logout
export const logoutUser = () => {
  clearTokens();
  window.location.href = "/login";
};
