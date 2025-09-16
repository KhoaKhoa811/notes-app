import type { Note } from "../types/note";

interface NoteListProps {
  notes: Note[];
  onEdit: (note: Note) => void;
  onDelete: (id: number) => void;
}

export default function NoteList({ notes, onEdit, onDelete }: NoteListProps) {
  return (
    <ul>
      {notes.map((note) => (
        <li key={note.id} style={{ margin: "10px 0" }}>
          <b>{note.title}</b> <br />
          <i>{note.content}</i> <br />
          <small>{new Date(note.createdAt).toLocaleString()}</small> <br />
          <button onClick={() => onEdit(note)}>✏️ Edit</button>
          <button onClick={() => onDelete(note.id)}>❌ Delete</button>
        </li>
      ))}
    </ul>
  );
}
