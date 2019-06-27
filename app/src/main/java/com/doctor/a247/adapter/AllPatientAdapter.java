package com.doctor.a247.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.Pescribe;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.activity.RawActivity;
import com.doctor.a247.model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllPatientAdapter extends RecyclerView.Adapter<AllPatientAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Patient> patientList;
    private ArrayList<Patient> fullList;

    public AllPatientAdapter(Context context, ArrayList<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
        this.fullList = new ArrayList<Patient>(patientList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.patient_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        try {
            Patient profile = patientList.get(i);
            viewHolder.patientNameTV.setText("Address : " + profile.getName());
            viewHolder.addressTV.setText("Address\n House : " + profile.getPresentAddress().getHouseNo()
                    + " Road No : " + profile.getPresentAddress().getRoadNo()
                    + " Village : " + profile.getPresentAddress().getVillage()
                    + " Post No : " + profile.getPresentAddress().getPost()
                    + " Thana : " + profile.getPresentAddress().getThana()
                    + " Division : " + profile.getPresentAddress().getDivision());
            viewHolder.bloodGroupTV.setText("Blood Group : " + profile.getBloodGroup());
            viewHolder.agetWeightTV.setText("Age " + profile.getAge() + "  |  Weight : " + profile.getWeight());
        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RawActivity.class);
                intent.putExtra("fragment", "patientProfile");
                intent.putExtra("patientId", patientList.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @Override
    public Filter getFilter() {
        return filterList;
    }

    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Patient> list = new ArrayList<Patient>();
            if (constraint == null || constraint.length()  == 0 ){
                list.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Patient patient : fullList){
                    if (patient.getPhoneNumber().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getHouseNo().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getRoadNo().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getVillage().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getPost().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getThana().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getZila().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPresentAddress().getDivision().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getHouseNo().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getRoadNo().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getVillage().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getPost().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getThana().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getZila().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getPermanantAddress().getDivision().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getFatherName().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getMotherName().toLowerCase().contains(filterPattern)){
                        list.add(patient);
                    }else if (String.valueOf(patient.getWeight()).contains(filterPattern)){
                        list.add(patient);
                    }else if (patient.getName().toLowerCase().contains(filterPattern)){
                        list.add(patient);
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
                patientList.clear();
                patientList.addAll((ArrayList<Patient>)results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView patientNameTV, agetWeightTV, bloodGroupTV, addressTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTV = itemView.findViewById(R.id.patientNameTV);
            agetWeightTV = itemView.findViewById(R.id.agetWeightTV);
            bloodGroupTV = itemView.findViewById(R.id.bloodGroupTV);
            addressTV = itemView.findViewById(R.id.addressTV);
        }
    }
}
