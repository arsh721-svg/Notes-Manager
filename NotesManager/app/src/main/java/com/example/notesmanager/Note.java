package com.example.notesmanager;

public class Note {
    public long id;
    public String title, content, category, createdAt;
    public boolean favorite;

    public Note(long id, String title, String content, String category,
                boolean favorite, String createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.favorite = favorite;
        this.createdAt = createdAt;
    }
}
