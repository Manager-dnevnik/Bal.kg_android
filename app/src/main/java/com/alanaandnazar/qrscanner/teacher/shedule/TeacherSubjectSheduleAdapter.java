package com.alanaandnazar.qrscanner.teacher.shedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Subject;
import com.alanaandnazar.qrscanner.parent.detailSubject.mark.DetailMarkActivity;
import com.alanaandnazar.qrscanner.teacher.TeacherActivity;
import com.alanaandnazar.qrscanner.teacher.children.ChildrenActivity;
import com.alanaandnazar.qrscanner.teacher.children.ChildrenMarkActivity;
import com.alanaandnazar.qrscanner.teacher.mark.MarkActivity;

import java.util.List;


public class TeacherSubjectSheduleAdapter extends RecyclerView.Adapter<TeacherSubjectSheduleAdapter.PersonViewHolder> {

    Context context;
    Subject vse;


    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView nameSubject, timeStart;
        Spinner spinner;

        PersonViewHolder(View itemView) {
            super(itemView);
            nameSubject = itemView.findViewById(R.id.tv_name_subject);
            timeStart = itemView.findViewById(R.id.tv_time_start);
            spinner = itemView.findViewById(R.id.subjectSpinner);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChildrenMarkActivity.class);
                    vse = listVse.get(getAdapterPosition());
                    intent.putExtra("class_id", id);
                    intent.putExtra("subject_id", vse.getId());
                    intent.putExtra("subject", vse.getName_subject());
                    Activity activity = (Activity) context;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                }
            });


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
        vse = new Subject();
        vse = listVse.get(i);

        personViewHolder.nameSubject.setText(vse.getName_subject());
        personViewHolder.timeStart.setText(vse.getTime_start());
    }

    @Override
    public int getItemCount() {
        return listVse.size();
    }

}