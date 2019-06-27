package com.doctor.a247.Patient_Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doctor.a247.A247;
import com.doctor.a247.Drawer_Fragments.PatientProfile;
import com.doctor.a247.Pojos.AppointmentedProfile;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.adapter.ProblemAdapter;
import com.doctor.a247.adapter.ProblemAdapter2;
import com.doctor.a247.model.Appoint;
import com.doctor.a247.model.Patient;
import com.doctor.a247.model.Problem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PProfile extends Fragment {

    private static final String TAG = PProfile.class.getSimpleName();

    private Context context;

    private TextView locationTV, nameTV, ageTV, weightTV, phoneTV, serialNoTV, timeTV, dateTV, bloodGroupTV, addressTV, permanantAddressTV, genderTV;
    private Appoint appoint;
    private RecyclerView problemsRC;
    private ArrayList<Problem> problems;
    private ProblemAdapter2 adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        appoint = PatientDetails.getAppoint();
        timeTV = v.findViewById(R.id.timeTV);
        serialNoTV = v.findViewById(R.id.serialNoTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        weightTV = v.findViewById(R.id.weightTV);
        ageTV = v.findViewById(R.id.ageTV);
        nameTV = v.findViewById(R.id.nameTV);
        dateTV = v.findViewById(R.id.dateTV);
        bloodGroupTV = v.findViewById(R.id.bloodGroupTV);
        problemsRC = v.findViewById(R.id.problemsRC);

        addressTV = v.findViewById(R.id.addressTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        bloodGroupTV = v.findViewById(R.id.bloodGroupTV);
        genderTV = v.findViewById(R.id.genderTV);
        permanantAddressTV = v.findViewById(R.id.permanantAddressTV);

        try {
            timeTV.setText("Appointment Time: "+appoint.getAppointTime());
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }
        try {
            dateTV.setText("Appointment Date: "+appoint.getAppointDate());
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }
        try {
            serialNoTV.setText("Serial Number : "+appoint.getSerial());
        }catch (Exception e){
            Log.d(TAG, "onDataChange: "+e.getMessage());
        }

        A247.getUserIDdatabaseReference(PatientDetails.getAppoint().getPatientid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null){
                    Patient patient = dataSnapshot.getValue(Patient.class);

                    try {
                        nameTV.setText("Name : " + patient.getName());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }try {
                        genderTV.setText("Gender : " + patient.getGender());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        ageTV.setText("Age: "+patient.getAge());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        weightTV.setText("Weight: "+patient.getWeight());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        phoneTV.setText("Contact: "+patient.getPhoneNumber());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }
                    try {
                        addressTV.setText("Present Address: \nHouse NO : " + patient.getPresentAddress().getHouseNo() + "Road No : " + patient.getPresentAddress().getRoadNo()
                                + " Village : " +  patient.getPresentAddress().getVillage() + " Post : "+  patient.getPresentAddress().getPost() + " Thana : " +  patient.getPresentAddress().getThana()
                                + " Zilla : " +  patient.getPresentAddress().getZila() + " Division : " +  patient.getPresentAddress().getDivision());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }try {
                        permanantAddressTV.setText("Permanant Address : \n House : " + patient.getPermanantAddress().getHouseNo()
                                + " Road No : " + patient.getPermanantAddress().getHouseNo()
                                + " Village : " + patient.getPermanantAddress().getVillage()
                                + " Post No : " + patient.getPermanantAddress().getPost()
                                + " Thana : " + patient.getPermanantAddress().getThana()
                                + " Zila : " + patient.getPermanantAddress().getZila()
                                + " Division : " + patient.getPermanantAddress().getDivision());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }try {
                        if (!patient.getBloodGroup().isEmpty()) {
                            bloodGroupTV.setText("Blood Groups: " + patient.getBloodGroup());
                        }
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: "+e.getMessage());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        A247.problems(PatientDetails.getAppoint().getPatientid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    problems = new ArrayList<Problem>();
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        problems.add(data.getValue(Problem.class));
                    }
                    if (problems.size() <= 0){
                        problemsRC.setVisibility(View.GONE);
                    }else {
                        problemsRC.setVisibility(View.VISIBLE);
                        adapter = new ProblemAdapter2(context, problems);
                        problemsRC.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

}
