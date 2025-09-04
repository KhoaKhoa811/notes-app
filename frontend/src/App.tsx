import { useEffect, useState } from "react";
import { getNotes, createNote, deleteNote } from "./api/notesApi";
import type { Note } from "./types/note";

function App() {
  const [notes, setNotes] = useState<Note[]>([]);
  const [newNote, setNewNote] = useState("");

  useEffect(() => {
    fetchNotes();
  }, []);

  const fetchNotes = async () => {
    const res = await getNotes();
    setNotes(res.data);
  };

  const handleAddNote = async () => {
    if (!newNote.trim()) return;
    await createNote({ title: newNote, content: "" });
    setNewNote("");
    fetchNotes();
  };

  const handleDelete = async (id: number) => {
    await deleteNote(id);
    fetchNotes();
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Notes App (TS + React)</h1>

      <input
        type="text"
        placeholder="Enter a note title..."
        value={newNote}
        onChange={(e) => setNewNote(e.target.value)}
      />
      <button onClick={handleAddNote}>Add</button>

      <ul>
        {notes.map((note) => (
          <li key={note.id}>
            {note.title}{" "}
            <button onClick={() => handleDelete(note.id)}>❌</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
