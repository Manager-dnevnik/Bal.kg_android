package com.alanaandnazar.qrscanner.teacher.home_work;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Homework;

import java.util.List;


public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.OrderViewHolder> {
    private List<Homework> orders;
    Context context;

    public HomeworkAdapter(Context context) {
        this.context = context;
    }

    public void removeItem(int position) {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);
        final OrderViewHolder holder = new OrderViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
//                    listener.onOrderClick(orders.get(pos), pos);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Homework homework = orders.get(position);
        holder.tvHomework.setText(homework.getHomework());
        holder.tvDate.setText(homework.getDate());

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public void updateItems(List<Homework> list) {
        orders = list;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvHomework, tvDate;
        Context context;

        public OrderViewHolder(View view) {
            super(view);
            context = view.getContext();

            tvHomework = view.findViewById(R.id.tv_homework);
            tvDate = view.findViewById(R.id.tv_date);
        }
    }

}