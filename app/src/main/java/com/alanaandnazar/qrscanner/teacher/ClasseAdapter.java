package com.alanaandnazar.qrscanner.teacher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Children;
import com.alanaandnazar.qrscanner.model.Classe;

import java.util.List;


public class ClasseAdapter extends RecyclerView.Adapter<ClasseAdapter.OrderViewHolder> {
    private List<Classe> orders;
    private OnOrderListener listener;

    public ClasseAdapter(OnOrderListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_children, parent, false);
        final OrderViewHolder holder = new OrderViewHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orders.get(pos), pos);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Classe classe = orders.get(position);
        holder.tv_title.setText(classe.getName());

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public void updateItems(List<Classe> list) {
        orders = list;
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
        void onOrderClick(Classe classe, int position);
    }
}