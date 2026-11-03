package com.example.notesmanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class EditNoteActivity extends AppCompatActivity {
    private NotesDbHelper db;
    private EditText titleInput, contentInput;
    private Spinner categorySpinner;
    private CheckBox favoriteCheck;
    private long noteId = -1;
    private final String[] categories = {"Personal", "College", "Work", "Important"};

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        db = new NotesDbHelper(this);
        titleInput = findViewById(R.id.titleInput);
        contentInput = findViewById(R.id.contentInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        favoriteCheck = findViewById(R.id.favoriteCheck);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(spinnerAdapter);

        noteId = getIntent().getLongExtra("note_id", -1);
        if (noteId != -1) loadNote();

        MaterialButton save = findViewById(R.id.saveButton);
        MaterialButton cancel = findViewById(R.id.cancelButton);
        save.setOnClickListener(v -> saveNote());
        cancel.setOnClickListener(v -> finish());
    }

    private void loadNote() {
        Note n = db.getNote(noteId);
        if (n == null) { finish(); return; }
        titleInput.setText(n.title);
        contentInput.setText(n.content);
        favoriteCheck.setChecked(n.favorite);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(n.category)) { categorySpinner.setSelection(i); break; }
        }
        setTitle("Edit Note");
    }

    private void saveNote() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();
        if (title.isEmpty()) { titleInput.setError("Please enter a title"); titleInput.requestFocus(); return; }
        if (content.isEmpty()) { contentInput.setError("Please enter note content"); contentInput.requestFocus(); return; }
        String category = categorySpinner.getSelectedItem().toString();
        boolean favorite = favoriteCheck.isChecked();
        if (noteId == -1) db.addNote(title, content, category, favorite);
        else db.updateNote(noteId, title, content, category, favorite);
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
