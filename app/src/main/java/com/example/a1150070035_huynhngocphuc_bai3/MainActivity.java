package com.example.a1150070035_huynhngocphuc_bai3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1150070035_huynhngocphuc_bai3.adapter.StudentAdapter;
import com.example.a1150070035_huynhngocphuc_bai3.database.StudentDatabaseHandler;
import com.example.a1150070035_huynhngocphuc_bai3.model.Student;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StudentDatabaseHandler db;
    private StudentAdapter adapter;
    private List<Student> students;
    private RecyclerView recyclerView;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new StudentDatabaseHandler(this);
        recyclerView = findViewById(R.id.recyclerViewStudents);
        btnAdd = findViewById(R.id.btnAdd);

        // Load dữ liệu từ DB
        students = new ArrayList<>(db.getAllStudents());

        // Adapter + RecyclerView
        adapter = new StudentAdapter(this, students, new StudentAdapter.OnStudentClickListener() {
            @Override
            public void onStudentClick(Student student, int position) {
                showEditStudentDialog(student, position); // nhấn để sửa
            }

            @Override
            public void onStudentLongClick(Student student, int position) {
                db.deleteStudent(student);
                students.remove(position); // xoá chính xác theo index
                adapter.notifyItemRemoved(position);
                Toast.makeText(MainActivity.this, "Đã xoá " + student.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Nút thêm sinh viên
        btnAdd.setOnClickListener(v -> showAddStudentDialog());
    }

    // Dialog thêm sinh viên
    private void showAddStudentDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_student, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);

        new AlertDialog.Builder(this)
                .setTitle("Thêm sinh viên")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();

                    if (!name.isEmpty()) {
                        Student student = new Student(name, email, phone);
                        db.addStudent(student);

                        // Reload list
                        students.clear();
                        students.addAll(db.getAllStudents());
                        adapter.notifyDataSetChanged();

                        Toast.makeText(this, "Đã thêm sinh viên", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Tên không được bỏ trống", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Dialog sửa sinh viên
    private void showEditStudentDialog(Student student, int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_student, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);

        // Gán dữ liệu cũ
        edtName.setText(student.getName());
        edtEmail.setText(student.getEmail());
        edtPhone.setText(student.getPhone());

        new AlertDialog.Builder(this)
                .setTitle("Sửa sinh viên")
                .setView(dialogView)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();

                    if (!name.isEmpty()) {
                        student.setName(name);
                        student.setEmail(email);
                        student.setPhone(phone);

                        db.updateStudent(student);
                        students.set(position, student); // update list tại vị trí
                        adapter.notifyItemChanged(position);

                        Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Tên không được bỏ trống", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
