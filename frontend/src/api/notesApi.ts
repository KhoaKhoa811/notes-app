import axios from "axios";
import type { Note } from "../types/note";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "/api",
});

export const getNotes = () => api.get<Note[]>("/notes");
export const createNote = (note: Partial<Note>) => api.post<Note>("/notes", note);
export const updateNote = (id: number, note: Partial<Note>) =>
  api.put<Note>(`/notes/${id}`, note);
export const deleteNote = (id: number) => api.delete(`/notes/${id}`);
