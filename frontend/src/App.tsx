import { useEffect, useState } from "react";
import { getNotes, createNote, updateNote, deleteNote } from "./api/NotesApi";
import type { Note } from "./types/note";
import NoteForm from "./components/NoteForm";
import NoteList from "./components/NoteList";

function App() {
  const [notes, setNotes] = useState<Note[]>([]);
  const [editingNote, setEditingNote] = useState<Note | null>(null);

  useEffect(() => {
    fetchNotes();
  }, []);

  const fetchNotes = async () => {
    const res = await getNotes();
    setNotes(res.data.data);
  };

  const handleSave = async (noteData: Pick<Note, "title" | "content">, id?: number) => {
    if (id) {
      await updateNote(id, noteData);
      setEditingNote(null);
    } else {
      await createNote(noteData);
    }
    fetchNotes();
  };

  const handleDelete = async (id: number) => {
    await deleteNote(id);
    fetchNotes();
  };

  const handleEdit = (note: Note) => {
    setEditingNote(note);
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Notes App (TS + React)</h1>
      <NoteForm
        onSave={handleSave}
        editingNote={editingNote}
        onCancel={() => setEditingNote(null)}
      />
      <NoteList notes={notes} onEdit={handleEdit} onDelete={handleDelete} />
    </div>
  );
}

export default App;
