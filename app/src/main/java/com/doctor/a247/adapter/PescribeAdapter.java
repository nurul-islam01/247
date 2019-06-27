package com.doctor.a247.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doctor.a247.Pojos.Pescribe;
import com.doctor.a247.R;
import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PescribeAdapter extends RecyclerView.Adapter <PescribeAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Pescribe> pescribes;

    public PescribeAdapter(Context context, ArrayList<Pescribe> pescribes) {
        this.context = context;
        this.pescribes = pescribes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.pescribe_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            Picasso.get().load(pescribes.get(i).getImageLink()).error(R.drawable.ic_error_black_24dp)
                    .placeholder(R.drawable.ic_error_black_24dp)
                    .into(viewHolder.pescribeIV);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pescribes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TouchImageView pescribeIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pescribeIV = itemView.findViewById(R.id.pescribeIV);

        }
    }
}
