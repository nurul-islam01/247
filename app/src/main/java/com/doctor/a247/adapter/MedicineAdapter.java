package com.doctor.a247.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.doctor.a247.Pojos.Medicine;
import com.doctor.a247.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Medicine> medicines;
    private ArrayList<Medicine> fullList;

    public MedicineAdapter(Context context, ArrayList<Medicine> medicines) {
        this.context = context;
        this.medicines = medicines;
        this.fullList = new ArrayList<>(medicines);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.medicine_raw, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        try {
            viewHolder.medicineNameTV.setText(medicines.get(i).getMedicineName());
            viewHolder.totalMedicineTV.setText(medicines.get(i).getTotalMedicine());
            viewHolder.medicineRulesTV.setText(medicines.get(i).getMedicineRules());
        }catch (Exception e){
            e.printStackTrace();
        }

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are you sure delete this");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        A247.medicine().child(medicines.get(i).getAppointmentId()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    @Override
    public Filter getFilter() {
        return filterList;
    }

    private Filter filterList = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Medicine> list = new ArrayList<Medicine>();
            if (constraint == null || constraint.length()  == 0 ){
                list.addAll(fullList);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Medicine medicine : fullList){
                    if (medicine.getMedicineName().toLowerCase().contains(filterPattern)){
                        list.add(medicine);
                    }else if (medicine.getMedicineRules().toLowerCase().contains(filterPattern)){
                        list.add(medicine);
                    }else if (medicine.getTotalMedicine().toLowerCase().contains(filterPattern)){
                        list.add(medicine);
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
                medicines.clear();
                medicines.addAll((ArrayList<Medicine>)results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView medicineNameTV, totalMedicineTV, medicineRulesTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineNameTV = itemView.findViewById(R.id.medicineNameTV);
            totalMedicineTV = itemView.findViewById(R.id.totalMedicineTV);
            medicineRulesTV = itemView.findViewById(R.id.medicineRulesTV);

        }
    }
}
