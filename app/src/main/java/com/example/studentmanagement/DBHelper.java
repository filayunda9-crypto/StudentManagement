
package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "StudentDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE students (" +
                "student_id TEXT PRIMARY KEY, " +
                "student_name TEXT, " +
                "major TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
        onCreate(db);
    }

    public boolean addStudent(String id, String name, String major) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("student_id", id);
        values.put("student_name", name);
        values.put("major", major);

        return db.insert("students", null, values) != -1;
    }

    public boolean editStudent(String id, String name, String major) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("student_name", name);
        values.put("major", major);

        return db.update("students", values,
                "student_id = ?", new String[]{id}) > 0;
    }

    public boolean deleteStudent(String id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete("students",
                "student_id = ?", new String[]{id}) > 0;
    }

    public Cursor viewStudents() {
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery("SELECT * FROM students", null);
    }
}
