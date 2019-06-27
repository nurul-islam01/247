package com.doctor.a247.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.AppointmentedProfile;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
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

public class AppointmentRequestAdapter extends RecyclerView.Adapter<AppointmentRequestAdapter.ViewHolder> implements Filterable
{

    private Context context;
    private ArrayList<Request> requestIdList;
    private ArrayList<Request> fullList;

    public AppointmentRequestAdapter(Context context, ArrayList<Request> requestIdList) {
        this.context = context;
        this.requestIdList = requestIdList;
        this.fullList = new ArrayList<>(requestIdList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.appointment_request_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {

        A247.getUserIDdatabaseReference(requestIdList.get(i).getPatientid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Patient profile = dataSnapshot.getValue(Patient.class);
                    try {
                        viewHolder.patientNameTV.setText(profile.getName());
                        viewHolder.addressTV.setText("Blood : " + profile.getBloodGroup());
                        viewHolder.agetWeightTV.setText("Age : "+profile.getAge()+"    Weight : "+profile.getWeight());
                        viewHolder.phoneTV.setText(profile.getPhoneNumber());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        viewHolder.confirmBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.confirmBT.setText("Confirmed");

                LayoutInflater inflater = LayoutInflater.from(context);
                View alerView = inflater.inflate(R.layout.appointment_confirm_dialogue, null);

                final DatePicker datePicker = alerView.findViewById(R.id.patientAppoinemntDateDP);
                final TimePicker timePicker = alerView.findViewById(R.id.appoinmentTimePicker);
                final EditText serialET = alerView.findViewById(R.id.serialNoET);

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setView(alerView).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(TextUtils.isEmpty(serialET.getText())){
                            Toast.makeText(context, "Serial Should Never Empty", Toast.LENGTH_SHORT).show();
                            return;
                        }else {

                            String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                            String time = timePicker.getHour() + ":" + timePicker.getMinute();
                            String serial = serialET.getText().toString();

                            Appoint appoint = new Appoint(requestIdList.get(i).getId(), requestIdList.get(i).getPatientName(), requestIdList.get(i).getDoctorId(), requestIdList.get(i).getPatientid(), date, time, serial);

                            A247.appointed().child(requestIdList.get(i).getId()).setValue(appoint)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                                                A247.request().child(requestIdList.get(i).getId()).removeValue();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();

            }
        });

        viewHolder.deleteBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        A247.request().child(requestIdList.get(i).getId()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestIdList.size();
    }

    @Override
    public Filter getFilter() {
        return filterList;
    }

    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Request> list = new ArrayList<Request>();
            if (constraint == null || constraint.length()  == 0 ){
                list.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Request request : fullList){
                    if (request.getPatientName().toLowerCase().contains(filterPattern)){
                        list.add(request);
                    }else if (request.getPatientid().toLowerCase().contains(filterPattern)){
                        list.add(request);
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
                requestIdList.clear();
                requestIdList.addAll((ArrayList<Request>)results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView patientNameTV, agetWeightTV, phoneTV, addressTV;
        private Button confirmBT;
        private ImageButton deleteBT;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientNameTV = itemView.findViewById(R.id.patientNameTV);
            agetWeightTV = itemView.findViewById(R.id.agetWeightTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            confirmBT = itemView.findViewById(R.id.confirmBT);
            deleteBT = itemView.findViewById(R.id.deleteBT);
        }
    }
}
