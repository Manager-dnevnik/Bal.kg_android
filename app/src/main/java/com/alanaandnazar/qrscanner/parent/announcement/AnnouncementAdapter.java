package com.alanaandnazar.qrscanner.parent.announcement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Announcement;

import java.util.List;


public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.AnnouncementViewHolder> {
    private List<Announcement> announcementList;
    private OnOrderListener listener;

    public AnnouncementAdapter(OnOrderListener listener) {
        this.listener = listener;
    }

    public void removeItem(int position) {
        announcementList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        final AnnouncementViewHolder holder = new AnnouncementViewHolder(itemView);
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onOrderClick(announcementList.get(pos), pos);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcementList.get(position);
        holder.tv_date.setText(announcement.getDate());
        holder.tv_about.setText(announcement.getAbout());

    }

    @Override
    public int getItemCount() {
        return announcementList == null ? 0 : announcementList.size();
    }

    public void updateItems(List<Announcement> list) {
        announcementList = list;
        notifyDataSetChanged();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        TextView tv_about, tv_date;
        Context context;

        public AnnouncementViewHolder(View view) {
            super(view);
            context = view.getContext();
            tv_about = view.findViewById(R.id.tv_about);
            tv_date = view.findViewById(R.id.tv_date);
        }
    }

    public interface OnOrderListener {
        void onOrderClick(Announcement announcement, int position);
    }
}