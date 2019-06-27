package com.doctor.a247.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.AppointmentedProfile;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.activity.SingleAppointment;
import com.doctor.a247.model.Appoint;
import com.doctor.a247.model.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Appoint> appoinmentList;
    private ArrayList<Appoint> fullList;

    public AppointmentsAdapter(Context context, ArrayList<Appoint> appoinmentList) {
        this.context = context;
        this.appoinmentList = appoinmentList;
        this.fullList = new ArrayList<>(appoinmentList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointments_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {


        viewHolder.serialandTimeTV.setText("Serial : " + appoinmentList.get(i).getSerial()+" | Time : "+appoinmentList.get(i).getAppointTime());
        viewHolder.dateTV.setText("Date : "+appoinmentList.get(i).getAppointDate());

        A247.getUserIDdatabaseReference(appoinmentList.get(i).getDoctorId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        Doctor doctorProfile = dataSnapshot.getValue(Doctor.class);
                        viewHolder.nameTV.setText(doctorProfile.getName());
                        Picasso.get().load(doctorProfile.getImageUrl()).placeholder(R.drawable.doctor_image_icon)
                                .error(R.drawable.doctor_image_icon).into(viewHolder.doctorIV);
                        viewHolder.specilityTV.setText("Specility : "+doctorProfile.getSpecalist());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are Your Sure Delete This")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                A247.appointed().child(appoinmentList.get(i).getId()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onFailure: " + e.getMessage());
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Cancled", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return false;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleAppointment.class);
                intent.putExtra("row", appoinmentList.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appoinmentList.size();
    }

    @Override
    public Filter getFilter() {
        return filterList;
    }

    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Appoint> list = new ArrayList<Appoint>();
            if (constraint == null || constraint.length()  == 0 ){
                list.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Appoint appoint : fullList){
                    if (appoint.getPatientName().toLowerCase().contains(filterPattern)){
                        list.add(appoint);
                    }else if (appoint.getPatientid().toLowerCase().contains(filterPattern)){
                        list.add(appoint);
                    }else if (appoint.getSerial().toLowerCase().contains(filterPattern)){
                        list.add(appoint);
                    }else if (appoint.getAppointTime().toLowerCase().contains(filterPattern)){
                        list.add(appoint);
                    }else if (appoint.getAppointDate().toLowerCase().contains(filterPattern)){
                        list.add(appoint);
                    }else if (appoint.getDoctorId().toLowerCase().contains(filterPattern)){
                        list.add(appoint);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = list;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                appoinmentList.clear();
                appoinmentList.addAll((ArrayList<Appoint>)results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTV, specilityTV, serialandTimeTV, dateTV;
        private ImageView doctorIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            specilityTV = itemView.findViewById(R.id.specilityTV);
            serialandTimeTV = itemView.findViewById(R.id.serialandTimeTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            doctorIV = itemView.findViewById(R.id.doctorIV);

        }
    }
}
