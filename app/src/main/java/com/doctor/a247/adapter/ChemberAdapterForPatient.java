package com.doctor.a247.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.model.Chamber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class ChemberAdapterForPatient extends RecyclerView.Adapter<ChemberAdapterForPatient.ViewHolder> {

    private Context context;
    private ArrayList<Chamber>  chambers;

    public ChemberAdapterForPatient(Context context, ArrayList<Chamber> chambers) {
        this.context = context;
        this.chambers = chambers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.chember_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        try {
            Chamber chamber = chambers.get(i);
            viewHolder.nameTV.setText(chamber.getHostpitalName());
            viewHolder.chemberAddressTV.setText(chamber.getChamberAddress());
            viewHolder.stayTimeTV.setText(chamber.getStayTime());
            StringBuilder builder = new StringBuilder();
            for (Integer day: chamber.getChamberDays()){
                if (day == 1){
                    builder.append(" Sat ");
                }else if (day == 2){
                    builder.append(" Sun ");
                }else if (day == 3){
                    builder.append(" Mon ");
                }else if (day == 4){
                    builder.append(" Tues ");
                }else if (day == 5){
                    builder.append(" Wed ");
                }else if (day == 6){
                    builder.append(" Thas ");
                }else if (day == 7){
                    builder.append(" Friday ");
                }
            }
            viewHolder.daysTV.setText(builder.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return chambers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, chemberAddressTV, stayTimeTV, daysTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            chemberAddressTV = itemView.findViewById(R.id.chemberAddressTV);
            stayTimeTV = itemView.findViewById(R.id.stayTimeTV);
            daysTV = itemView.findViewById(R.id.daysTV);
        }
    }
}
