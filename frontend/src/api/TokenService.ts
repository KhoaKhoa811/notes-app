// src/api/TokenService.ts

export const getAccessToken = (): string | null => {
  const token = localStorage.getItem("accessToken");
  if (!token || token === "null" || token === "undefined" || token.trim() === "") {
    return null;
  }
  return token;
};

export const getRefreshToken = (): string | null => {
  const token = localStorage.getItem("refreshToken");
  if (!token || token === "null" || token === "undefined" || token.trim() === "") {
    return null;
  }
  return token;
};

export const setTokens = (accessToken: string, refreshToken?: string) => {
  if (accessToken && accessToken.trim() !== "") {
    localStorage.setItem("accessToken", accessToken);
  }
  if (refreshToken && refreshToken.trim() !== "") {
    localStorage.setItem("refreshToken", refreshToken);
  }
};

export const clearTokens = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
};
