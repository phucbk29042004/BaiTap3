package com.example.a1150070035_huynhngocphuc_bai3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1150070035_huynhngocphuc_bai3.R;
import com.example.a1150070035_huynhngocphuc_bai3.model.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private Context context;
    private List<Student> studentList;
    private OnStudentClickListener listener;

    // Interface callback có cả position
    public interface OnStudentClickListener {
        void onStudentClick(Student student, int position);
        void onStudentLongClick(Student student, int position);
    }

    public StudentAdapter(Context context, List<Student> studentList, OnStudentClickListener listener) {
        this.context = context;
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvName.setText(student.getName());
        holder.tvEmail.setText(student.getEmail());
        holder.tvPhone.setText(student.getPhone());

        // Click bình thường → sửa
        holder.itemView.setOnClickListener(v -> listener.onStudentClick(student, position));

        // Long click → xoá
        holder.itemView.setOnLongClickListener(v -> {
            listener.onStudentLongClick(student, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
