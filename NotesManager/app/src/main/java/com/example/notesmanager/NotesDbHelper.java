package com.example.notesmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotesDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notes_manager.db";
    private static final int DB_VERSION = 1;

    public NotesDbHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, content TEXT NOT NULL, " +
                "category TEXT NOT NULL DEFAULT 'Personal', " +
                "favorite INTEGER NOT NULL DEFAULT 0, created_at TEXT NOT NULL, " +
                "updated_at TEXT NOT NULL)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }

    private String now() {
        return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());
    }

    public long addNote(String title, String content, String category, boolean favorite) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title); v.put("content", content); v.put("category", category);
        v.put("favorite", favorite ? 1 : 0); v.put("created_at", now()); v.put("updated_at", now());
        return db.insert("notes", null, v);
    }

    public int updateNote(long id, String title, String content, String category, boolean favorite) {
        ContentValues v = new ContentValues();
        v.put("title", title); v.put("content", content); v.put("category", category);
        v.put("favorite", favorite ? 1 : 0); v.put("updated_at", now());
        return getWritableDatabase().update("notes", v, "id=?", new String[]{String.valueOf(id)});
    }

    public int deleteNote(long id) {
        return getWritableDatabase().delete("notes", "id=?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Note> getNotes(String query, boolean favoritesOnly) {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT id,title,content,category,favorite,created_at FROM notes " +
                "WHERE (title LIKE ? OR content LIKE ?) " +
                (favoritesOnly ? "AND favorite=1 " : "") + "ORDER BY updated_at DESC";
        String term = "%" + query + "%";
        try (Cursor c = db.rawQuery(sql, new String[]{term, term})) {
            while (c.moveToNext()) {
                notes.add(new Note(c.getLong(0), c.getString(1), c.getString(2),
                        c.getString(3), c.getInt(4) == 1, c.getString(5)));
            }
        }
        return notes;
    }

    public Note getNote(long id) {
        try (Cursor c = getReadableDatabase().rawQuery(
                "SELECT id,title,content,category,favorite,created_at FROM notes WHERE id=?",
                new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new Note(c.getLong(0), c.getString(1), c.getString(2),
                    c.getString(3), c.getInt(4) == 1, c.getString(5));
        }
        return null;
    }
}
