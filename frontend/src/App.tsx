import { useEffect, useState } from "react";
import { getNotes, createNote, updateNote, deleteNote } from "./api/NotesApi";
import type { Note } from "./types/note";

function App() {
  const [notes, setNotes] = useState<Note[]>([]);
  const [newTitle, setNewTitle] = useState("");
  const [newContent, setNewContent] = useState("");
  const [editingId, setEditingId] = useState<number | null>(null);

  useEffect(() => {
    fetchNotes();
  }, []);

  const fetchNotes = async () => {
    const res = await getNotes();
    setNotes(res.data);
  };

  const handleAddNote = async () => {
    if (!newTitle.trim()) return;
    await createNote({ title: newTitle, content: newContent });
    setNewTitle("");
    setNewContent("");
    fetchNotes();
  };

  const handleDelete = async (id: number) => {
    await deleteNote(id);
    fetchNotes();
  };

  const handleEdit = (note: Note) => {
    setEditingId(note.id);
    setNewTitle(note.title);
    setNewContent(note.content);
  };

  const handleUpdate = async () => {
    if (!editingId) return;
    await updateNote(editingId, { title: newTitle, content: newContent });
    setEditingId(null);
    setNewTitle("");
    setNewContent("");
    fetchNotes();
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Notes App (TS + React)</h1>

      <input
        type="text"
        placeholder="Enter note title..."
        value={newTitle}
        onChange={(e) => setNewTitle(e.target.value)}
      />
      <br />
      <textarea
        placeholder="Enter note content..."
        value={newContent}
        onChange={(e) => setNewContent(e.target.value)}
      />
      <br />
      {editingId ? (
        <button onClick={handleUpdate}>Update</button>
      ) : (
        <button onClick={handleAddNote}>Add</button>
      )}

      <ul>
        {notes.map((note) => (
          <li key={note.id} style={{ margin: "10px 0" }}>
            <b>{note.title}</b> <br />
            <i>{note.content}</i> <br />
            <small>{new Date(note.createdAt).toLocaleString()}</small> <br />
            <button onClick={() => handleEdit(note)}>✏️ Edit</button>
            <button onClick={() => handleDelete(note.id)}>❌ Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
