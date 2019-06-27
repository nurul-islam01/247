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
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.model.Appoint;
import com.doctor.a247.model.Patient;
import com.doctor.a247.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class AppointmentedAdapter extends RecyclerView.Adapter<AppointmentedAdapter.ViewHolder> implements Filterable {

    private static final String TAG = AppointmentedAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Appoint> rowList;
    private ArrayList<Appoint> fullList;

    public AppointmentedAdapter(Context context, ArrayList<Appoint> rowList) {
        this.context = context;
        this.rowList = rowList;
        try {
            this.fullList = new ArrayList<>(rowList);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointmented_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        Appoint profile = null;
        try {
            profile = rowList.get(i);
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            viewHolder.serialNoTV.setText("Serial : " + profile.getSerial());
        } catch (Exception e) {
            Log.d(TAG, "onDataChange: " + e.getMessage());
        }

        try {
            viewHolder.dateTV.setText("Date : " + profile.getAppointDate());
        } catch (Exception e) {
            Log.d(TAG, "onDataChange: " + e.getMessage());
        }
        try {
            viewHolder.timeTV.setText("Time : " + profile.getAppointTime());
        } catch (Exception e) {
            Log.d(TAG, "onDataChange: " + e.getMessage());
        }
        if(profile.getPatientid() != null) {
            A247.getUserIDdatabaseReference(profile.getPatientid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null) {
                        Patient patient = dataSnapshot.getValue(Patient.class);

                        try {
                            viewHolder.phoneTV.setText(patient.getPhoneNumber());
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: " + e.getMessage());
                        }
                        try {
                            viewHolder.nameTV.setText("Name : " + patient.getName());
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: " + e.getMessage());
                        }
                        try {
                            viewHolder.weightTV.setText("Weight : " + patient.getWeight());
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: " + e.getMessage());
                        }
                        try {
                            viewHolder.ageTV.setText("Age : " + patient.getAge());
                        } catch (Exception e) {
                            Log.d(TAG, "onDataChange: " + e.getMessage());
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure delete this item")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                A247.appointed().child(rowList.get(i).getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                return false;
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PatientDetails.class);
                intent.putExtra("patientId", rowList.get(i));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return rowList.size();
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
                rowList.clear();
                rowList.addAll((ArrayList<Appoint>)results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };




    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTV, ageTV, weightTV, phoneTV, serialNoTV, timeTV, dateTV, bloodGroupTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTV = itemView.findViewById(R.id.timeTV);
            serialNoTV = itemView.findViewById(R.id.serialNoTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            ageTV = itemView.findViewById(R.id.ageTV);
            nameTV = itemView.findViewById(R.id.nameTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            bloodGroupTV = itemView.findViewById(R.id.bloodGroupTV);

        }
    }
}
