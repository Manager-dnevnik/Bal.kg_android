package com.alanaandnazar.qrscanner.parent.mark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Subject;
import com.alanaandnazar.qrscanner.parent.detailSubject.mark.DetailMarkActivity;
import com.google.gson.Gson;

import java.util.List;


public class MarkSubjectScheduleAdapter extends RecyclerView.Adapter<MarkSubjectScheduleAdapter.PersonViewHolder> {

    Context context;
    Subject mSubject;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView subjectName, timeStart, mark;

        PersonViewHolder(View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.tv_name_subject);
            timeStart = itemView.findViewById(R.id.tv_time_start);
            mark = itemView.findViewById(R.id.mark);
            mark.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailMarkActivity.class);
                mSubject = subjectsOfTheDay.get(getAdapterPosition());
                intent.putExtra("id", id);
                intent.putExtra("subject_id", mSubject.getId());
                intent.putExtra("subject", mSubject.getName_subject());
                intent.putExtra("subject_date", mSubject.getDate());
                Activity activity = (Activity) context;
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            });

        }

    }

    public void removeAt(int position) {
        subjectsOfTheDay.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, subjectsOfTheDay.size());
    }

    List<Subject> subjectsOfTheDay;
    int id;

    public MarkSubjectScheduleAdapter(Context context, List<Subject> listVse, int id) {
        this.subjectsOfTheDay = listVse;
        this.context = context;
        this.id = id;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_subject_shedule, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        mSubject = new Subject();
        mSubject = subjectsOfTheDay.get(i);
        Log.e("ОЦЕНКИ: ", mSubject.getName_subject() + " - " + mSubject.getTime_start() + " - " + mSubject.getMark());

        personViewHolder.subjectName.setText(mSubject.getName_subject());
        personViewHolder.timeStart.setText(mSubject.getTime_start());

        if (mSubject.getMark() != null) {
            personViewHolder.mark.setText(mSubject.getMark());
            Log.e("SUBJECT_INFO", "MARKSUBJECADP: " + new Gson().toJson(mSubject));
        } else {
            personViewHolder.mark.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return subjectsOfTheDay.size();
    }

}