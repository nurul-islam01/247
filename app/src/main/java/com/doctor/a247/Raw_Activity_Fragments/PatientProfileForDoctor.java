package com.doctor.a247.Raw_Activity_Fragments;


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
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.adapter.ProblemAdapter;
import com.doctor.a247.model.Patient;
import com.doctor.a247.model.Problem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PatientProfileForDoctor extends Fragment {

    private static final String TAG = PatientProfileForDoctor.class.getSimpleName();
    private Context context;
    private String patientId;

    private TextView nameTV, ageTV, weightTV, phoneTV, bloodGroupTV, genderTV, addressTV, permanantAddressTV;
    private RecyclerView problemsRC;

    private ArrayList<Problem> problems;
    private ProblemAdapter adapter;

    public PatientProfileForDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_profile_for_doctor, container, false);
        nameTV = v.findViewById(R.id.nameTV);
        ageTV = v.findViewById(R.id.ageTV);
        weightTV = v.findViewById(R.id.weightTV);
        addressTV = v.findViewById(R.id.addressTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        bloodGroupTV = v.findViewById(R.id.bloodGroupTV);
        genderTV = v.findViewById(R.id.genderTV);

        problemsRC = v.findViewById(R.id.problemsRC);
        permanantAddressTV = v.findViewById(R.id.permanantAddressTV);

        A247.getProfileById(patientId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){

                    try {
                        Patient profile = dataSnapshot.getValue(Patient.class);
                        nameTV.setText(profile.getName());
                        ageTV.setText("Age : "+profile.getAge() + " Years");
                        weightTV.setText("Weight : "+profile.getWeight() +" KG");
                        phoneTV.setText("Phone : " + profile.getPhoneNumber());
                        genderTV.setText("Gender : "+profile.getGender());

                        addressTV.setText("Present Address : \n House : " + profile.getPresentAddress().getHouseNo()
                                + " Road No : " + profile.getPresentAddress().getHouseNo()
                                + " Village : " + profile.getPresentAddress().getVillage()
                                + " Post No : " + profile.getPresentAddress().getPost()
                                + " Thana : " + profile.getPresentAddress().getThana()
                                + " Zila : " + profile.getPresentAddress().getZila()
                                + " Division : " + profile.getPresentAddress().getDivision());

                        permanantAddressTV.setText("Permanant Address : \n House : " + profile.getPermanantAddress().getHouseNo()
                                + " Road No : " + profile.getPermanantAddress().getHouseNo()
                                + " Village : " + profile.getPermanantAddress().getVillage()
                                + " Post No : " + profile.getPermanantAddress().getPost()
                                + " Thana : " + profile.getPermanantAddress().getThana()
                                + " Zila : " + profile.getPermanantAddress().getZila()
                                + " Division : " + profile.getPermanantAddress().getDivision());

                        if (!profile.getBloodGroup().isEmpty()){
                            bloodGroupTV.setText("Blood Group : " + profile.getBloodGroup());
                        }else {
                            bloodGroupTV.setText("Blood Group not set");
                        }

                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: " + e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        A247.problems(patientId).addValueEventListener(new ValueEventListener() {
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
                        adapter = new ProblemAdapter(context, problems);
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
        Bundle bundle = getArguments();
        patientId = bundle.getString("patientId");
    }
}
