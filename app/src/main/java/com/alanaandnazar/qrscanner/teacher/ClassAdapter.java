package com.alanaandnazar.qrscanner.teacher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Class;

import java.util.List;


public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.OrderViewHolder> {
    private List<Class> classes;
    private OnOrderListener listener;

    public ClassAdapter(OnOrderListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        classes.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        final OrderViewHolder holder = new OrderViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onOrderClick(classes.get(pos), pos);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Class classe = classes.get(position);
        holder.tv_title.setText(classe.getName());

    }

    @Override
    public int getItemCount() {
        return classes == null ? 0 : classes.size();
    }

    public void updateItems(List<Class> list) {
        classes = list;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        Context context;

        public OrderViewHolder(View view) {
            super(view);
            context = view.getContext();
            tv_title = view.findViewById(R.id.tv_title);
        }
    }

    public interface OnOrderListener {
        void onOrderClick(Class classe, int position);
    }
}