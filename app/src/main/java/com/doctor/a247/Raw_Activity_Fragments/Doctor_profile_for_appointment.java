package com.doctor.a247.Raw_Activity_Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.adapter.ChemberAdapter;
import com.doctor.a247.adapter.ChemberAdapterForPatient;
import com.doctor.a247.model.Chamber;
import com.doctor.a247.model.Doctor;
import com.doctor.a247.model.Patient;
import com.doctor.a247.model.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Doctor_profile_for_appointment extends Fragment {

    private Context context;
    private ImageView userphoto;
    private TextView userName, userdegination, doctorPhone, ageTV, presentAddressTV, permanantAddressTV, exprienceTV;
    private TextView exEndDateTV, exStartDateTV, exDepartmentNameTV, exHosptialNameTV, fatherNameTV, motherNameTV;
    private Button appoinmentBT;

    private String doctorId;
    private RecyclerView allChemberRV;
    private CardView noChamberCV;
    private RecyclerView.Adapter adapter;
    private ArrayList<Chamber> chambers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_doctor_profile_for_appointment, container, false);

        exprienceTV = v.findViewById(R.id.exprienceTV);
        permanantAddressTV = v.findViewById(R.id.permanantAddressTV);
        presentAddressTV = v.findViewById(R.id.presentAddressTV);
        userphoto = v.findViewById(R.id.userphoto);
        userName = v.findViewById(R.id.userName);
        userdegination = v.findViewById(R.id.userdegination);
        doctorPhone = v.findViewById(R.id.doctorPhone);
        ageTV = v.findViewById(R.id.ageTV);
        appoinmentBT = v.findViewById(R.id.appoinmentBT);
        exEndDateTV = v.findViewById(R.id.exEndDateTV);
        exStartDateTV = v.findViewById(R.id.exStartDateTV);
        exDepartmentNameTV = v.findViewById(R.id.exDepartmentNameTV);
        exHosptialNameTV = v.findViewById(R.id.exHosptialNameTV);
        noChamberCV = v.findViewById(R.id.noChamberCV);
        allChemberRV = v.findViewById(R.id.allChemberRV);
        motherNameTV = v.findViewById(R.id.motherNameTV);
        fatherNameTV = v.findViewById(R.id.fatherNameTV);

        A247.getUserIDdatabaseReference(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        Doctor profile = dataSnapshot.getValue(Doctor.class);
                        userName.setText(profile.getName());
                        userdegination.setText(profile.getDegination()+"\n"+profile.getSpecalist());
                        ageTV.setText("Gender : "+ profile.getGender() + " Age : " + profile.getAge());
                        doctorPhone.setText(profile.getPhoneNumber());
                        exprienceTV.setText("Experience : \nHospital Name : " + profile.getExperience().getHostpitalName()
                                + "\nDepartment Name : " + profile.getExperience().getDeptName()
                                + "\nStart Date : " + profile.getExperience().getStartDate()
                                + "\nEnd date : " + profile.getExperience().getEndDate());
                        presentAddressTV.setText(  "Present Address : \nHouse NO : " + profile.getPresentAddress().getHouseNo() + "Road No : " + profile.getPresentAddress().getRoadNo()
                                + " Village : " +  profile.getPresentAddress().getVillage() + " Post : "+  profile.getPresentAddress().getPost() + " Thana : " +  profile.getPresentAddress().getThana()
                                + " Zilla : " +  profile.getPresentAddress().getZila() + " Division : " +  profile.getPresentAddress().getDivision());
                        permanantAddressTV.setText( "Permanant Address : \nHouse NO : " + profile.getPermanantAddress().getHouseNo() + "Road No : " + profile.getPermanantAddress().getRoadNo()
                                + " Village : " +  profile.getPermanantAddress().getVillage() + " Post : "+  profile.getPermanantAddress().getPost() + " Thana : " +  profile.getPermanantAddress().getThana()
                                + " Zilla : " +  profile.getPermanantAddress().getZila() + " Division : " +  profile.getPermanantAddress().getDivision());

                        Picasso.get().load(profile.getImageUrl()).placeholder(R.drawable.doctor_image_icon)
                                .error(R.drawable.doctor_image_icon).into(userphoto);

                        exDepartmentNameTV.setText( "Department : " + profile.getExperience().getDeptName());
                        exHosptialNameTV.setText("Hospital : " + profile.getExperience().getHostpitalName());
                        exStartDateTV.setText("Start Date : " + profile.getExperience().getStartDate());
                        exEndDateTV.setText("End Date : " + profile.getExperience().getEndDate());
                        fatherNameTV.setText("Father : " + profile.getFatherName());
                        motherNameTV.setText("Mother : " + profile.getMotherName());

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        appoinmentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Sent Request For Appoinment");
                alertDialog.setPositiveButton("Send",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = A247.getDatabaseReference().push().getKey();

                        Request request = new Request(id, null, doctorId, A247.getUserId());
                        A247.getProfileById(A247.getUserId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null){
                                    Patient patient = dataSnapshot.getValue(Patient.class);
                                    if (patient.getName() != null){
                                        request.setPatientName(patient.getName());
                                        A247.request().child(id).setValue(request);
                                        Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Request Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });


        A247.chamber(doctorId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    chambers = new ArrayList<Chamber>();
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        chambers.add(data.getValue(Chamber.class));
                    }
                    if (chambers.size() <= 0){
                        allChemberRV.setVisibility(View.GONE);
                        noChamberCV.setVisibility(View.VISIBLE);
                    }else {
                        allChemberRV.setVisibility(View.VISIBLE);
                        noChamberCV.setVisibility(View.GONE);
                        adapter = new ChemberAdapterForPatient(context, chambers);
                        allChemberRV.setAdapter(adapter);
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
       doctorId = bundle.getString("doctorId");

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
