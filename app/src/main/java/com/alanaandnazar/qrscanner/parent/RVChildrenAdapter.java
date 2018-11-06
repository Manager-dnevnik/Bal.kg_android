package com.alanaandnazar.qrscanner.parent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alanaandnazar.qrscanner.R;
import com.alanaandnazar.qrscanner.model.Children;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RVChildrenAdapter extends RecyclerView.Adapter<RVChildrenAdapter.PersonViewHolder> {

    Context context;
    Children vse;

    public class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        Button btn_delivered;
        Button btn_navigation;

        PersonViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, OSMapsActivity.class);
                    vse = listVse.get(getAdapterPosition());
//                    intent.putExtra("id", vse.getId());
//                    Activity activity = (Activity) context;
//                    activity.startActivityForResult(intent, 123);
//                    activity.overridePendingTransition(0, 0);
//                    ((OSMapsActivity) context).getMapsCenterMarker(vse.getGeolocation());
                }
            });

        }

    }

    public void removeAt(int position) {
        listVse.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listVse.size());
    }

    List<Children> listVse;

    public RVChildrenAdapter(Context context, List<Children> listVse) {
        this.listVse = listVse;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_children, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, int i) {
        vse = new Children();
        vse = listVse.get(i);

        personViewHolder.tv_title.setText(vse.getLast_name()+" "+vse.getFirst_name()+" "+vse.getSecond_name());
    }

    @Override
    public int getItemCount() {
        return listVse.size();
    }

}