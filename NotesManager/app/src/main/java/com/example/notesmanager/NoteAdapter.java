package com.example.notesmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    public interface Listener { void onNoteClick(Note note); void onNoteLongClick(Note note); }
    private final ArrayList<Note> notes = new ArrayList<>();
    private final Listener listener;

    public NoteAdapter(Listener listener) { this.listener = listener; }
    public void setNotes(ArrayList<Note> items) { notes.clear(); notes.addAll(items); notifyDataSetChanged(); }

    @NonNull @Override public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull NoteHolder h, int position) {
        Note n = notes.get(position);
        h.title.setText(n.title);
        h.preview.setText(n.content);
        h.meta.setText(n.category + "  •  " + n.createdAt + (n.favorite ? "  •  ★ Favorite" : ""));
        h.itemView.setOnClickListener(v -> listener.onNoteClick(n));
        h.itemView.setOnLongClickListener(v -> { listener.onNoteLongClick(n); return true; });
    }

    @Override public int getItemCount() { return notes.size(); }

    static class NoteHolder extends RecyclerView.ViewHolder {
        TextView title, preview, meta;
        NoteHolder(View v) {
            super(v);
            title = v.findViewById(R.id.noteTitle);
            preview = v.findViewById(R.id.notePreview);
            meta = v.findViewById(R.id.noteMeta);
        }
    }
}
