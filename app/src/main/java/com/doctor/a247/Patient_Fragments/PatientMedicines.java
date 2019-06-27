package com.doctor.a247.Patient_Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.Medicine;
import com.doctor.a247.R;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.adapter.MedicineAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientMedicines extends Fragment {

    private static final String TAG = PatientMedicines.class.getSimpleName();
    private Context context;

    private CardView medicineAddCV;
    private RecyclerView allPatientRV;

    private ArrayList<Medicine> medicines;
    private MedicineAdapter adapter;

    public PatientMedicines() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_medicines, container, false);
        medicineAddCV = v.findViewById(R.id.medicineAddCV);
        allPatientRV = v.findViewById(R.id.allPatientRV);

        medicineAddCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View addView = LayoutInflater.from(context).inflate(R.layout.medicine_add_dialogue, null);
                final EditText medicineNameET, totalMedicineET, medicineRules;
                medicineNameET = addView.findViewById(R.id.medicineNameET);
                totalMedicineET = addView.findViewById(R.id.totalMedicineET);
                medicineRules = addView.findViewById(R.id.medicineRules);

                AlertDialog.Builder addDialoge = new AlertDialog.Builder(context);
                addDialoge.setView(addView);
                addDialoge.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String name = medicineNameET.getText().toString().trim();
                            String totalMedicine = totalMedicineET.getText().toString().trim();
                            String medicinesRule = medicineRules.getText().toString().trim();
                            if (!name.isEmpty() && !totalMedicine.isEmpty() && !medicinesRule.isEmpty()){
                                saveMedicine(name, totalMedicine, medicinesRule);
                            }else {
                                Toast.makeText(context, "Saved Failed for incomplete field", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.d(TAG, "onClick: " + e.getMessage());
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                addDialoge.show();
            }
        });

        try {
            A247.medicine().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        medicines = new ArrayList<Medicine>();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            Medicine medicine = d.getValue(Medicine.class);
                            if (TextUtils.equals(medicine.getPatientId(), PatientDetails.getAppoint().getPatientid())){
                                medicines.add(d.getValue(Medicine.class));
                            }
                        }
                        if (medicines.size() <= 0){
                            allPatientRV.setVisibility(View.GONE);
                        }else {
                            allPatientRV.setVisibility(View.VISIBLE);
                            adapter = new MedicineAdapter(context, medicines);
                            allPatientRV.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.d(TAG, "onCreateView: " + e.getMessage());
        }


        return v;
    }

    private void saveMedicine(String name, String totalMedicine, String medicineRules){
        Medicine medicine = new Medicine();
        medicine.setMedicineName(name);
        medicine.setMedicineRules(medicineRules);
        medicine.setTotalMedicine(totalMedicine);
        medicine.setAppointmentId(PatientDetails.getAppoint().getId());
        medicine.setPatientId(PatientDetails.getAppoint().getPatientid());
        String pushId = A247.medicine().push().getKey();
        medicine.setMedicineId(pushId);

        A247.medicine().child(pushId).setValue(medicine)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Saved Medicine", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Not Saved, Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Saved Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }
}
