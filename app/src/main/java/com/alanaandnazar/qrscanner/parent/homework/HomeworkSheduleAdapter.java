package com.alanaandnazar.qrscanner.parent.homework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Shedule;
import com.alanaandnazar.qrscanner.parent.mark.MarkSubjectSheduleAdapter;

import java.util.List;


public class HomeworkSheduleAdapter extends RecyclerView.Adapter<HomeworkSheduleAdapter.OrderViewHolder> {

    private List<Shedule> moves;
    private OnOrderListener listener;
    Context context;
    int id = 0;

    public HomeworkSheduleAdapter(Context context, OnOrderListener listener, int id) {
        this.listener = listener;
        this.context = context;
        this.id = id;
    }

    public void removeItem(int position) {
        moves.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shedule, parent, false);
        final OrderViewHolder holder = new OrderViewHolder(itemView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(moves.get(pos), pos);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Shedule shedule = moves.get(position);
        holder.tv_day_name.setText(shedule.getDay_name());


        if (shedule.getList_subjects() != null) {
            holder.rvSubjectSheduleAdapter = new HomeworkSubjectSheduleAdapter(context, shedule.getList_subjects(), id);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setAdapter(holder.rvSubjectSheduleAdapter);
        }

    }

    @Override
    public int getItemCount() {
        return moves == null ? 0 : moves.size();
    }

    public void updateItems(List<Shedule> list) {
        moves = list;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day_name;
        RecyclerView recyclerView;
        Context context;
        HomeworkSubjectSheduleAdapter rvSubjectSheduleAdapter;

        public OrderViewHolder(View view) {
            super(view);
            context = view.getContext();
            tv_day_name = view.findViewById(R.id.tv_day_name);
            recyclerView = view.findViewById(R.id.recyclerView);

        }
    }

    public interface OnOrderListener {
        void onOrderClick(Shedule shedule, int position);
    }
}