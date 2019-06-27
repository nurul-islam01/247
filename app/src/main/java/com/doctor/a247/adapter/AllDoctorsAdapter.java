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
import android.widget.ImageView;
import android.widget.TextView;

import com.doctor.a247.R;
import com.doctor.a247.activity.RawActivity;
import com.doctor.a247.model.Doctor;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllDoctorsAdapter extends RecyclerView.Adapter<AllDoctorsAdapter.ViewHolder> implements Filterable {

    private Context context;

    private ArrayList<Doctor> doctorList;
    private ArrayList<Doctor> fullList;

    public AllDoctorsAdapter(Context context, ArrayList<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        this.fullList = new ArrayList<>(doctorList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.doctor_list_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {


        try {
            Doctor profile = doctorList.get(i);
            viewHolder.nameTV.setText(profile.getName());
            viewHolder.phoneTV.setText(profile.getPhoneNumber());
            viewHolder.designationTV.setText(profile.getDegination());
            viewHolder.specilityTV.setText(profile.getSpecalist());
            Picasso.get().load(profile.getImageUrl()).placeholder(R.drawable.doctor_image_icon).error(R.drawable.doctor_image_icon).into(viewHolder.doctorIV);
        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RawActivity.class);
                intent.putExtra("fragment", "doctorProfile");
                intent.putExtra("doctorId", doctorList.get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }


    @Override
    public Filter getFilter() {
        return filterList;
    }

    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Doctor> list = new ArrayList<>();
            if (constraint == null || constraint.length()  == 0 ){
                list.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Doctor doctor : fullList){
                    if (doctor.getName().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPhoneNumber().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getSpecalist().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getDegination().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getExperience().getDeptName().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getExperience().getHostpitalName().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getGender().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPresentAddress().getHouseNo().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPresentAddress().getVillage().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPresentAddress().getDivision().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPermanantAddress().getHouseNo().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPermanantAddress().getVillage().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
                    }else if (doctor.getPermanantAddress().getDivision().toLowerCase().contains(filterPattern)){
                        list.add(doctor);
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
                doctorList.clear();
                doctorList.addAll((ArrayList<Doctor>)results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

         ImageView doctorIV;
         TextView nameTV, designationTV, specilityTV, phoneTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            designationTV = itemView.findViewById(R.id.designationTV);
            specilityTV = itemView.findViewById(R.id.specilityTV);
            phoneTV = itemView.findViewById(R.id.phoneTV);
            doctorIV = itemView.findViewById(R.id.doctorIV);

        }
    }

}
