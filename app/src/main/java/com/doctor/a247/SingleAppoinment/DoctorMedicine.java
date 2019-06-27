package com.doctor.a247.SingleAppoinment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.Medicine;
import com.doctor.a247.R;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.activity.SingleAppointment;
import com.doctor.a247.adapter.MedicineAdapter;
import com.doctor.a247.model.Appoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorMedicine extends Fragment {

    private static final String TAG = DoctorMedicine.class.getSimpleName();
    private Context context;

    private RecyclerView allPatientRV;
    private ArrayList<Medicine> medicines;
    private MedicineAdapter adapter;

    public DoctorMedicine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_medicine, container, false);
        allPatientRV = v.findViewById(R.id.allPatientRV);

        try {
            A247.medicine().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        medicines = new ArrayList<Medicine>();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            Medicine medicine = d.getValue(Medicine.class);
                            if (TextUtils.equals(medicine.getAppointmentId(), SingleAppointment.getAppoint().getId())){
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }
}
