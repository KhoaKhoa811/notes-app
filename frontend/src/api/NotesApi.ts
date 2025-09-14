import api from "./api";
import type { Note } from "../types/note";

export const getNotes = () => api.get<Note[]>("/v1/notes");

export const createNote = (note: Pick<Note, "title" | "content">) =>
  api.post<Note>("/v1/notes", note);

export const updateNote = (id: number, note: Pick<Note, "title" | "content">) =>
  api.put<Note>(`/v1/notes/${id}`, note);

export const deleteNote = (id: number) => api.delete(`/v1/notes/${id}`);
