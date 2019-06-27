package com.doctor.a247.Drawer_Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.activity.ProfileEditActivity;
import com.doctor.a247.activity.Registration;
import com.doctor.a247.adapter.ChemberAdapter;
import com.doctor.a247.model.Appoint;
import com.doctor.a247.model.Chamber;
import com.doctor.a247.model.Doctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorProfile extends Fragment {

    private static final String TAG = DoctorProfile.class.getSimpleName();

    private CircleImageView userphoto;
    private TextView userName, userdegination, doctorPhone, doctorchember, doctorSpeciality,
            ageTV, genderTV, fatherNameTV, motherNameTV, presentAddress, permanantAddress,
            exHosptialNameTV, exDepartmentNameTV, exStartDateTV, exEndDateTV, addChemberTV, profileEditBT, profileDeletBT;
    private RecyclerView allChemberRV;
    private CardView noChamberCV;

    private Context context;
    private ProfileDeleteAsync async;

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
        View v = inflater.inflate(R.layout.fragment_doctor_profile, container, false);

        noChamberCV = v.findViewById(R.id.noChamberCV);
        allChemberRV = v.findViewById(R.id.allChemberRV);
        addChemberTV = v.findViewById(R.id.addChemberTV);
        exEndDateTV = v.findViewById(R.id.exEndDateTV);
        exStartDateTV = v.findViewById(R.id.exStartDateTV);
        exDepartmentNameTV = v.findViewById(R.id.exDepartmentNameTV);
        exHosptialNameTV = v.findViewById(R.id.exHosptialNameTV);
        permanantAddress = v.findViewById(R.id.permanantAddress);
        presentAddress = v.findViewById(R.id.presentAddress);
        motherNameTV = v.findViewById(R.id.motherNameTV);
        fatherNameTV = v.findViewById(R.id.fatherNameTV);
        genderTV = v.findViewById(R.id.genderTV);
        ageTV = v.findViewById(R.id.ageTV);

        userphoto = v.findViewById(R.id.userphoto);
        userName = v.findViewById(R.id.userName);
        userdegination = v.findViewById(R.id.userdegination);
        doctorPhone = v.findViewById(R.id.doctorPhone);
        doctorSpeciality = v.findViewById(R.id.userSpeciality);
        profileEditBT = v.findViewById(R.id.profileEditBT);
        profileDeletBT = v.findViewById(R.id.profileDeletBT);

        noChamberCV.setVisibility(View.GONE);
        addChemberTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView hospitalNameET, chamberAddressET, stayTimeET;
                CheckBox days1, days2, days3, days4, days5, days6, days7;

                View view = LayoutInflater.from(context).inflate(R.layout.chember_input, null);

                days1 = view.findViewById(R.id.days1);
                days2 = view.findViewById(R.id.days2);
                days3 = view.findViewById(R.id.days3);
                days4 = view.findViewById(R.id.days4);
                days5 = view.findViewById(R.id.days5);
                days6 = view.findViewById(R.id.days6);
                days7 = view.findViewById(R.id.days7);

                hospitalNameET = view.findViewById(R.id.hospitalNameET);
                chamberAddressET = view.findViewById(R.id.chamberAddressET);
                stayTimeET = view.findViewById(R.id.stayTimeET);

                ArrayList<Integer> days = new ArrayList<Integer>();
                days1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(1);
                        }
                    }
                });
                days2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(2);
                        }
                    }
                });
                days3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(3);
                        }
                    }
                });
                days5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(4);
                        }
                    }
                });
                days5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(5);
                        }
                    }
                });
                days6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(6);
                        }
                    }
                });
                days7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            days.add(7);
                        }
                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id;
                        String hostpitalName;
                        String chamberAddress;
                        String stayTime;
                        // save data
                        try {
                            hostpitalName = hospitalNameET.getText().toString().trim();
                            chamberAddress = chamberAddressET.getText().toString().trim();
                            stayTime = stayTimeET.getText().toString().trim();
                            if (!TextUtils.isEmpty(hostpitalName)
                                && !TextUtils.isEmpty(chamberAddress)
                                && !TextUtils.isEmpty(stayTime)
                                && days.size() != 0
                            ){
                                String pushKey = A247.getDatabaseReference().push().getKey();
                                Chamber chamber = new Chamber(pushKey,hostpitalName, chamberAddress, stayTime, days);
                                A247.chamber(A247.getUserId()).child(pushKey).setValue(chamber)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });




                            }else {
                                Toast.makeText(context, "Not Saved", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            Log.d(TAG, "onClick: " + e.getMessage());
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        profileDeletBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Are Your Sure Deleting Profile")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                async = new ProfileDeleteAsync();
                                async.execute();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

            }
        });

        profileEditBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileEditActivity.class);
                intent.putExtra("profile", A247.TYPE_DOCTOR);
                startActivity(intent);
            }
        });

        A247.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        Doctor profile = dataSnapshot.getValue(Doctor.class);
                        userName.setText(profile.getName());
                        userdegination.setText(profile.getDegination());

                        doctorPhone.setText("Phone : " + profile.getPhoneNumber());
                        doctorSpeciality.setText(profile.getSpecalist());
                        fatherNameTV.setText("Father : " + profile.getFatherName());
                        motherNameTV.setText("Mother : " + profile.getMotherName());
                        ageTV.setText("Age : " + profile.getAge());
                        genderTV.setText("Gender : " + profile.getGender());
                        presentAddress.setText(  "Present Address : \nHouse NO : " + profile.getPresentAddress().getHouseNo() + "Road No : " + profile.getPresentAddress().getRoadNo()
                            + " Village : " +  profile.getPresentAddress().getVillage() + " Post : "+  profile.getPresentAddress().getPost() + " Thana : " +  profile.getPresentAddress().getThana()
                            + " Zilla : " +  profile.getPresentAddress().getZila() + " Division : " +  profile.getPresentAddress().getDivision());
                        permanantAddress.setText( "Permanant Address : \nHouse NO : " + profile.getPermanantAddress().getHouseNo() + "Road No : " + profile.getPermanantAddress().getRoadNo()
                            + " Village : " +  profile.getPermanantAddress().getVillage() + " Post : "+  profile.getPermanantAddress().getPost() + " Thana : " +  profile.getPermanantAddress().getThana()
                            + " Zilla : " +  profile.getPermanantAddress().getZila() + " Division : " +  profile.getPermanantAddress().getDivision());

                        Picasso.get().load(profile.getImageUrl()).placeholder(R.drawable.doctor_image_icon)
                                .error(R.drawable.doctor_image_icon).into(userphoto);

                        exDepartmentNameTV.setText( "Department : " + profile.getExperience().getDeptName());
                        exHosptialNameTV.setText("Hospital : " + profile.getExperience().getHostpitalName());
                        exStartDateTV.setText("Start Date : " + profile.getExperience().getStartDate());
                        exEndDateTV.setText("End Date : " + profile.getExperience().getEndDate());

//                        if (profile.getChambers() != null){
//                            Toast.makeText(context, "" + profile.getChambers().size(), Toast.LENGTH_SHORT).show();
//
//                        }

                    }catch (Exception e){
                        Log.d("TAG", "onDataChange1: " + e.getMessage());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        A247.chamber(A247.getUserId()).addValueEventListener(new ValueEventListener() {
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
                        adapter = new ChemberAdapter(context, chambers);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                A247.getFirebaseAuth().signOut();
                startActivity(new Intent(context, Registration.class));
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void profileDelete(){
        A247.userPhoneNumber(A247.getUser().getPhoneNumber()).removeValue();
        A247.allDoctors().child(A247.getUserId()).removeValue();
        A247.chamber(A247.getUserId()).removeValue();
        A247.removeUserType().removeValue();

        A247.appointed().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Appoint appoint = data.getValue(Appoint.class);
                        if (TextUtils.equals(appoint.getDoctorId(), A247.getUserId())){
                            A247.appointed().child(appoint.getId()).removeValue();
                        }

                    }
                    Toast.makeText(context, "Delete All", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        A247.deleteUser().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                A247.getFirebaseAuth().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        try {
                            A247.getFirebaseAuth().signOut();
                            startActivity(new Intent(context, Registration.class));
                            getActivity().finish();
                        }catch (Exception ee){
                            Log.d("TAG", "onSuccess: " +ee.getMessage());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        try {
                            A247.getFirebaseAuth().signOut();
                            startActivity(new Intent(context, Registration.class));
                            getActivity().finish();
                        }catch (Exception ee){
                            Log.d("TAG", "onFailure: " + ee.getMessage());
                        }

                    }
                });



            }
        });
    }

    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context_ = getActivity();
        context = context_;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private class ProfileDeleteAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            profileDelete();
            return null;
        }
    }
}
