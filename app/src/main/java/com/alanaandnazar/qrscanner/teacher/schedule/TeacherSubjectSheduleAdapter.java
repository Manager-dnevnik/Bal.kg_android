package com.alanaandnazar.qrscanner.teacher.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Subject;
import com.alanaandnazar.qrscanner.teacher.CreateHomeWorkActivity;
import com.alanaandnazar.qrscanner.teacher.children.ChildrenMarkActivity;

import java.util.List;


public class TeacherSubjectSheduleAdapter extends RecyclerView.Adapter<TeacherSubjectSheduleAdapter.PersonViewHolder> {

    Context context;
    Subject mSubject;


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView nameSubject, timeStart;
        Spinner spinner;

        PersonViewHolder(View itemView) {
            super(itemView);
            nameSubject = itemView.findViewById(R.id.tv_name_subject);
            timeStart = itemView.findViewById(R.id.tv_time_start);
            spinner = itemView.findViewById(R.id.subjectSpinner);

            itemView.setOnClickListener(v -> {
                mSubject = listVse.get(getAdapterPosition());
                showTipDialog(id, mSubject.getId(), mSubject.getName_subject(), mSubject.getDate());
            });


        }

        public void showTipDialog(int id, int subject_id, String name, String subject_date) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = li.inflate(R.layout.allert_dialog, null);


            builder.setView(dialogView);

            Button mark = dialogView.findViewById(R.id.mark);
            TextView homework = dialogView.findViewById(R.id.homework);

            final AlertDialog dialog = builder.create();
            mark.setOnClickListener(view -> {
                Intent intent = new Intent(context, ChildrenMarkActivity.class);
                intent.putExtra("class_id", id);
                intent.putExtra("subject_id", subject_id);
                intent.putExtra("subject", name);
                intent.putExtra("subject_date", subject_date);
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                dialog.cancel();
            });
            homework.setOnClickListener(view -> {
                Intent intent = new Intent(context, CreateHomeWorkActivity.class);
                intent.putExtra("class_id", id);
                intent.putExtra("subject_id", subject_id);
                intent.putExtra("name", name);
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                dialog.cancel();
            });
            dialog.show();
        }


    }

    public void removeAt(int position) {
        listVse.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listVse.size());
    }

    List<Subject> listVse;
    int id;

    public TeacherSubjectSheduleAdapter(Context context, List<Subject> listVse, int id) {
        this.listVse = listVse;
        this.context = context;
        this.id = id;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subject_shedule_teacher, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        mSubject = new Subject();
        mSubject = listVse.get(i);

        personViewHolder.nameSubject.setText(mSubject.getName_subject());
        personViewHolder.timeStart.setText(mSubject.getTime_start());
    }

    @Override
    public int getItemCount() {
        return listVse.size();
    }

}