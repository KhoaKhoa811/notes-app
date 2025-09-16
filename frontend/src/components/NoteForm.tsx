import { useState, useEffect } from "react";
import type { Note } from "../types/note";

interface NoteFormProps {
  onSave: (note: Pick<Note, "title" | "content">, id?: number) => void;
  editingNote?: Note | null;
  onCancel?: () => void;
}

export default function NoteForm({ onSave, editingNote, onCancel }: NoteFormProps) {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  useEffect(() => {
    if (editingNote) {
      setTitle(editingNote.title);
      setContent(editingNote.content);
    } else {
      setTitle("");
      setContent("");
    }
  }, [editingNote]);

  const handleSubmit = () => {
    if (!title.trim()) return;
    onSave({ title, content }, editingNote?.id);
    setTitle("");
    setContent("");
  };

  return (
    <div style={{ marginBottom: "20px" }}>
      <input
        type="text"
        placeholder="Enter note title..."
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <br />
      <textarea
        placeholder="Enter note content..."
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <br />
      <button onClick={handleSubmit}>{editingNote ? "Update" : "Add"}</button>
      {editingNote && onCancel && (
        <button onClick={onCancel} style={{ marginLeft: "10px" }}>
          Cancel
        </button>
      )}
    </div>
  );
}
