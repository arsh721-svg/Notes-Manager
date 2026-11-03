package com.example.notesmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements NoteAdapter.Listener {
    private NotesDbHelper db;
    private NoteAdapter adapter;
    private EditText searchInput;
    private TextView emptyText, countText;
    private boolean favoritesOnly = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new NotesDbHelper(this);
        searchInput = findViewById(R.id.searchInput);
        emptyText = findViewById(R.id.emptyText);
        countText = findViewById(R.id.countText);
        RecyclerView recycler = findViewById(R.id.notesRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this);
        recycler.setAdapter(adapter);

        FloatingActionButton add = findViewById(R.id.addButton);
        add.setOnClickListener(v -> openEditor(-1));

        MaterialButton all = findViewById(R.id.allButton);
        MaterialButton favorites = findViewById(R.id.favoritesButton);
        all.setOnClickListener(v -> { favoritesOnly = false; refresh(); });
        favorites.setOnClickListener(v -> { favoritesOnly = true; refresh(); });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int before, int count) { refresh(); }
            @Override public void afterTextChanged(Editable e) {}
        });
    }

    @Override protected void onResume() { super.onResume(); refresh(); }

    private void refresh() {
        if (db == null || adapter == null) return;
        java.util.ArrayList<Note> notes = db.getNotes(searchInput == null ? "" : searchInput.getText().toString(), favoritesOnly);
        adapter.setNotes(notes);
        countText.setText(notes.size() + (notes.size() == 1 ? " note" : " notes"));
        emptyText.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void openEditor(long id) {
        Intent i = new Intent(this, EditNoteActivity.class);
        if (id != -1) i.putExtra("note_id", id);
        startActivity(i);
    }

    @Override public void onNoteClick(Note note) { openEditor(note.id); }

    @Override public void onNoteLongClick(Note note) {
        new AlertDialog.Builder(this).setTitle("Delete note?")
                .setMessage("This note will be permanently deleted.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (d, w) -> { db.deleteNote(note.id); refresh(); })
                .show();
    }
}
