package com.alanaandnazar.qrscanner.parent.detailSubject.mark;

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
import com.alanaandnazar.qrscanner.model.Children;
import com.alanaandnazar.qrscanner.model.Mark;

import java.util.List;


public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.OrderViewHolder> {
    private List<Mark> orders;
    Context context;

    public MarkAdapter(Context context) {
        this.context = context;
    }

    public void removeItem(int position) {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark, parent, false);
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
        Mark mark = orders.get(position);
        ViewGroup.LayoutParams params = holder.card_view.getLayoutParams();

        Log.e("MARK", mark.getComm() + " " + mark.getDate());
        if (mark.getType_mark().equals("part")) {
            holder.linePart.setVisibility(View.VISIBLE);
        } else {
            holder.linePart.setVisibility(View.GONE);
        }
        holder.tvPart.setText(mark.getPart());
        holder.tvComm.setText(mark.getComm());
        holder.tvDate.setText(mark.getDate());
//        if(mark.getMark().equals("1")){
//            holder.tvMark.setText("Н/Б");
//        }else{
//            holder.tvMark.setText(mark.getMark());
//        }


        if (mark.getMark()==null || mark.getMark().isEmpty()){
            params.height = 0;
            params.width = 0;
            holder.card_view.setLayoutParams(params);
           // return;
        } else {
            holder.tvMark.setText(mark.getMark());

        }
//        if (mark.getMark().isEmpty()){
//            params.height = 0;
//            params.width = 0;
//            holder.card_view.setLayoutParams(params);        }

    }

    @Override
    public int getItemCount() {
        return orders == null ? 0 : orders.size();
    }

    public void updateItems(List<Mark> list) {
        orders = list;
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
    }

}