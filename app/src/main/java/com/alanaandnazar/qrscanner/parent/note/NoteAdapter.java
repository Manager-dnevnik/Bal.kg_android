package com.alanaandnazar.qrscanner.parent.note;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Note;
import com.alanaandnazar.qrscanner.model.Subject;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.OrderViewHolder> {
    private List<Note> orders;
    private OnOrderListener listener;

    public NoteAdapter(OnOrderListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
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
        Note note = orders.get(position);
        holder.tv_date.setText(note.getDate());
        holder.tv_about.setText(note.getAbout());

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public void updateItems(List<Note> list) {
        orders = list;
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
        void onOrderClick(Note note, int position);
    }
}