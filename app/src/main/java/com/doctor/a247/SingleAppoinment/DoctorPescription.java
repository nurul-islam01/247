package com.doctor.a247.SingleAppoinment;

import android.content.Context;
import android.net.Uri;
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
import com.doctor.a247.Pojos.Pescribe;
import com.doctor.a247.R;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.activity.SingleAppointment;
import com.doctor.a247.adapter.PescribeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class DoctorPescription extends Fragment {

    private static final String TAG = DoctorPescription.class.getSimpleName();
    private Context context;

    private RecyclerView allPescribeRV;
    private ArrayList<Pescribe> pescribes;
    private PescribeAdapter adapter;

    public DoctorPescription() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_pescription, container, false);
        allPescribeRV = v.findViewById(R.id.allAllPescribeRV);

        try {
            A247.prescription()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null){
                                pescribes = new ArrayList<Pescribe>();
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    Pescribe pescribe = d.getValue(Pescribe.class);
                                    if (TextUtils.equals(pescribe.getAppointmentId(), SingleAppointment.getAppoint().getId())) {
                                        pescribes.add(d.getValue(Pescribe.class));
                                    }
                                }
                                if (pescribes.size() > 0){
                                    adapter = new PescribeAdapter(context, pescribes);
                                    allPescribeRV.setAdapter(adapter);
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
