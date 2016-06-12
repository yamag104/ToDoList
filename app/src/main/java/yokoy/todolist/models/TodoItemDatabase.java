package yokoy.todolist.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yokoy on 6/11/16.
 */
public class TodoItemDatabase extends SQLiteOpenHelper{

    private static TodoItemDatabase sInstance;
    SQLiteDatabase db;

    // Database Info
    private static final String DATABASE_NAME = "todoItemDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_TODOITEMS = "todoItems";

    // To do Items Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_TITLE = "title";
    private static final String KEY_TODO_COMPELTED = "completed";

    public static synchronized TodoItemDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new TodoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOITEMS_TABLE = "CREATE TABLE " + TABLE_TODOITEMS +
                "('" +
                KEY_TODO_ID + "' INTEGER PRIMARY KEY, '" + // Define a primary key
                KEY_TODO_TITLE + "' TEXT, '" + KEY_TODO_COMPELTED + "' INTEGER" + ");";

        db.execSQL(CREATE_TODOITEMS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEMS);
            onCreate(db);
        }
    }

    // Insert a post into the database
    public void addToDo(ToDo todo) {
        // Create and/or open the database for writing
        db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_TODO_TITLE, todo.title);
            values.put(KEY_TODO_COMPELTED, 0);
            db = getReadableDatabase();
            Cursor dbCursor = db.query(TABLE_TODOITEMS, null, null, null, null, null, null);
            String[] columnNames = dbCursor.getColumnNames();
            System.out.println(Arrays.toString(columnNames));
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TODOITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println("Error while trying to add todo to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<ToDo> getAllToDos() {
        List<ToDo> todos = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s", TABLE_TODOITEMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDo newToDo = new ToDo();
                    newToDo.id = cursor.getInt(cursor.getColumnIndex(KEY_TODO_ID));
                    newToDo.title = cursor.getString(cursor.getColumnIndex(KEY_TODO_TITLE));
                    newToDo.completed = cursor.getInt(cursor.getColumnIndex(KEY_TODO_COMPELTED)) == 1 ? true : false;
                    todos.add(newToDo);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            System.out.println("Error while trying to get todos from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;
    }

    public int updateToDo(ToDo todo) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODO_TITLE, todo.title);

        // Updating profile title for todo with that Id
        return db.update(TABLE_TODOITEMS, values, KEY_TODO_ID + " = ?",
                new String[] { String.valueOf(todo.id) });
    }

    public boolean deleteToDo(ToDo todo) {
        db = this.getWritableDatabase();
        return db.delete(TABLE_TODOITEMS, KEY_TODO_ID + "=" + todo.id, null) > 0;
    }

    public void deleteAllToDos() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TODOITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            System.out.println("Error while trying to delete all todos");
        } finally {
            db.endTransaction();
        }
    }

    public void dropTable() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEMS);
    }
}
