
package com.example.studentmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;

public class MainActivity extends Activity {

    EditText etName, etStudentId;
    Spinner spMajor;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etStudentId = findViewById(R.id.etStudentId);
        spMajor = findViewById(R.id.spMajor);

        db = new DBHelper(this);

        Button add = findViewById(R.id.btnAdd);
        Button edit = findViewById(R.id.btnEdit);
        Button delete = findViewById(R.id.btnDelete);
        Button view = findViewById(R.id.btnView);

        // ADD
        add.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String id = etStudentId.getText().toString().trim();
            String major = spMajor.getSelectedItem().toString();

            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Fill in all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.addStudent(id, name, major)) {
                Toast.makeText(this, "Data added",
                        Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(this, "Student ID already exists",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // EDIT
        edit.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String id = etStudentId.getText().toString().trim();
            String major = spMajor.getSelectedItem().toString();

            if (name.isEmpty() || id.isEmpty()) {
                Toast.makeText(this, "Fill in all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.editStudent(id, name, major)) {
                Toast.makeText(this, "Data updated",
                        Toast.LENGTH_SHORT).show();
                clearForm();
            } else {
                Toast.makeText(this, "Student ID not found",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // DELETE
        delete.setOnClickListener(v -> {
            String id = etStudentId.getText().toString().trim();

            if (id.isEmpty()) {
                Toast.makeText(this, "Select a student first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setNegativeButton("CANCEL", null)
                    .setPositiveButton("DELETE", (dialog, which) -> {
                        if (db.deleteStudent(id)) {
                            Toast.makeText(this, "Data deleted",
                                    Toast.LENGTH_SHORT).show();
                            clearForm();
                        } else {
                            Toast.makeText(this, "Student ID not found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });

        // VIEW: Select student to edit
        view.setOnClickListener(v -> {
            Cursor cursor = db.viewStudents();

            ArrayList<String> studentList = new ArrayList<>();
            ArrayList<String> studentIds = new ArrayList<>();
            ArrayList<String> studentNames = new ArrayList<>();
            ArrayList<String> studentMajors = new ArrayList<>();

            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String major = cursor.getString(2);

                studentIds.add(id);
                studentNames.add(name);
                studentMajors.add(major);

                studentList.add(
                        "Name: " + name +
                                "\nID: " + id +
                                "\nMajor: " + major
                );
            }

            cursor.close();

            if (studentList.isEmpty()) {
                Toast.makeText(this, "No student data",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    studentList
            );

            new AlertDialog.Builder(this)
                    .setTitle("Select Student")
                    .setAdapter(adapter, (dialog, position) -> {
                        etName.setText(studentNames.get(position));
                        etStudentId.setText(studentIds.get(position));

                        String major = studentMajors.get(position);
                        int majorPosition =
                                major.equals("Information Systems") ? 1 : 0;

                        spMajor.setSelection(majorPosition);

                        Toast.makeText(this, "Student selected",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("CLOSE", null)
                    .show();
        });
    }

    private void clearForm() {
        etName.setText("");
        etStudentId.setText("");
        spMajor.setSelection(0);
    }
}
