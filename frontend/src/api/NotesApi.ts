import api from "./api";
import type { Note } from "../types/note";

interface ApiResponse<T> {
  code: number;
  data: T;
}

export const getNotes = () => api.get<ApiResponse<Note[]>>("/v1/notes");

export const createNote = (note: Pick<Note, "title" | "content">) =>
  api.post<ApiResponse<Note>>("/v1/notes", note);

export const updateNote = (id: number, note: Pick<Note, "title" | "content">) =>
  api.put<ApiResponse<Note>>(`/v1/notes/${id}`, note);

export const deleteNote = (id: number) =>
  api.delete<ApiResponse<null>>(`/v1/notes/${id}`);
