package com.alanaandnazar.qrscanner.parent.detailSubject.mark;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Mark;

import java.util.List;


public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.OrderViewHolder> {
    private List<Mark> marks;
    Context context;

    public MarkAdapter(Context context) {
        this.context = context;
    }

    public void removeItem(int position) {
        marks.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark, parent, false);
        final OrderViewHolder holder = new OrderViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
//                    listener.onOrderClick(orders.get(pos), pos);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.onBind(marks.get(position));
    }

    @Override
    public int getItemCount() {
        return marks != null ? marks.size() : 0;
    }

    public void setItems(List<Mark> list) {
        marks = list;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvPart, tvMark, tvComm, tvDate;
        Context context;
        CardView card_view;
        LinearLayout linePart;

        public OrderViewHolder(View view) {
            super(view);
            context = view.getContext();
            tvComm = view.findViewById(R.id.tv_comm);
            tvMark = view.findViewById(R.id.tv_mark);
            tvPart = view.findViewById(R.id.tv_part);
            tvDate = view.findViewById(R.id.tv_date);
            linePart = view.findViewById(R.id.line_part);
            card_view = view.findViewById(R.id.card_view);
        }

        public void onBind(Mark mark) {
            ViewGroup.LayoutParams params = card_view.getLayoutParams();
            Log.e("MARK", mark.getComm() + " " + mark.getDate());
            if (mark.getType_mark().equals("part")) {
                linePart.setVisibility(View.VISIBLE);
            } else {
                linePart.setVisibility(View.GONE);
            }
            tvPart.setText(mark.getPart());
            tvComm.setText(mark.getComm());
            tvDate.setText(mark.getDate());

            if (mark.getMark() == null || mark.getMark().isEmpty()) {
                params.height = 0;
                params.width = 0;
                card_view.setLayoutParams(params);
                // return;
            } else {
                tvMark.setText(mark.getMark());

            }
        }
    }

}