# Notes Manager (BCA Android Project)

A beginner-friendly Android notes application built with Java, XML, SQLite, and RecyclerView.

## Included features
- Create, view, edit, and delete notes
- Search note title and content
- Categories: Personal, College, Work, Important
- Mark notes as favorites and view favorites separately
- Local offline SQLite storage
- Empty-input validation

## Open in Android Studio
1. Extract `NotesManager.zip`.
2. Open Android Studio and choose **Open**.
3. Select the extracted `NotesManager` folder.
4. Allow Gradle sync and install any requested Android SDK components.
5. Run the app on an emulator or connected Android phone.

## How to use
- Tap **+** to add a note.
- Tap a note to edit it.
- Long-press a note to delete it after confirmation.
- Use the search box to search titles and content.
- Tap **★ Favorites** to show only favorite notes.
- Choose a category and optionally mark a note as favorite in the editor.

## Project structure
- `MainActivity.java`: home screen, search, favorites, delete confirmation
- `EditNoteActivity.java`: create and edit notes
- `NotesDbHelper.java`: SQLite database and CRUD operations
- `NoteAdapter.java`: RecyclerView adapter
- `Note.java`: note data model
- `res/layout`: XML layouts
- `res/drawable`: card and input backgrounds
- `res/values`: colors and theme

## Notes
- Package name: `com.example.notesmanager`
- Minimum Android version: Android 6.0 (API 23)
- Internet permission is not required.
- This is a starter college project. Test it on your target Android version and customize the UI before submission.
