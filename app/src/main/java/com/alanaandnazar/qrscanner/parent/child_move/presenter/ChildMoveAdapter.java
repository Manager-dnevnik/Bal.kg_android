package com.alanaandnazar.qrscanner.parent.child_move.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.ChildMove;
import com.alanaandnazar.qrscanner.model.Children;

import java.util.List;


public class ChildMoveAdapter extends RecyclerView.Adapter<ChildMoveAdapter.OrderViewHolder> {
    private List<ChildMove> moves;
    private OnOrderListener listener;

    public ChildMoveAdapter(OnOrderListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        moves.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child_move, parent, false);
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
        ChildMove childMove = moves.get(position);
        holder.tv_about.setText(childMove.getAbout());
        holder.tv_date.setText(childMove.getDate());
    }

    @Override
    public int getItemCount() {
        return moves == null ? 0 : moves.size();
    }

    public void updateItems(List<ChildMove> list) {
        moves = list;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tv_about, tv_date;
        Context context;

        public OrderViewHolder(View view) {
            super(view);
            context = view.getContext();
            tv_about = view.findViewById(R.id.tv_about);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public interface OnOrderListener {
        void onOrderClick(ChildMove childMove, int position);
    }
}