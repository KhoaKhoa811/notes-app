import axios from "axios";
import type { Note } from "../types/note";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "/api",
});

// Interceptor: tự động gắn token nếu có
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken"); // lưu khi login thành công
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const getNotes = () => api.get<Note[]>("/notes");
export const createNote = (note: Partial<Note>) => api.post<Note>("/notes", note);
export const updateNote = (id: number, note: Partial<Note>) =>
  api.put<Note>(`/notes/${id}`, note);
export const deleteNote = (id: number) => api.delete(`/notes/${id}`);
