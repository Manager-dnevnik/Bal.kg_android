package com.alanaandnazar.qrscanner.parent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Student;

import java.util.List;


public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder> {
    private List<Student> students;
    private onStudentClickListener listener;

    public StudentsAdapter(onStudentClickListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        students.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        final StudentsViewHolder holder = new StudentsViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onStudentClick(students.get(pos), pos);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolder holder, int position) {
        Student student = students.get(position);
        String studentInfo = student.getLast_name() + " " + student.getFirst_name() + " " + student.getSecond_name();
        Log.e("STUDENT_INFO", studentInfo);
        holder.tv_title.setText(studentInfo);
    }

    @Override
    public int getItemCount() {
        return students == null ? 0 : students.size();
    }

    public void updateItems(List<Student> list) {
        students = list;
        notifyDataSetChanged();
    }

    static class StudentsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        Context context;

        public StudentsViewHolder(View view) {
            super(view);
            context = view.getContext();
            tv_title = view.findViewById(R.id.tv_title);
        }
    }

    public interface onStudentClickListener {
        void onStudentClick(Student student, int position);
    }
}