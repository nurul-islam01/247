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
import android.support.v4.view.PagerAdapter;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.activity.ProfileEditActivity;
import com.doctor.a247.activity.Registration;
import com.doctor.a247.adapter.ProblemAdapter;
import com.doctor.a247.model.Patient;
import com.doctor.a247.model.Problem;
import com.doctor.a247.model.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PatientProfile extends Fragment {

    private static final String TAG = PagerAdapter.class.getSimpleName();

    private Context context;

    private TextView nameTV, ageTV, weightTV, fatherNameTV, motherNameTV, phoneTV, bloodGroupTV, genderTV, addressTV, permanantAddressTV, addProblemTV;
    private Button editProfileBT, userDeleteBT;
    private ProfileDeleteAsync async;
    private RecyclerView problemsRC;

    private ArrayList<Problem> problems;
    private ProblemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        fatherNameTV = v.findViewById(R.id.fatherNameTV);
        motherNameTV = v.findViewById(R.id.motherNameET);
        addProblemTV = v.findViewById(R.id.addProblemTV);
        nameTV = v.findViewById(R.id.nameTV);
        ageTV = v.findViewById(R.id.ageTV);
        weightTV = v.findViewById(R.id.weightTV);
        addressTV = v.findViewById(R.id.addressTV);
        phoneTV = v.findViewById(R.id.phoneTV);
        bloodGroupTV = v.findViewById(R.id.bloodGroupTV);
        genderTV = v.findViewById(R.id.genderTV);
        editProfileBT = v.findViewById(R.id.editProfileBT);
        userDeleteBT = v.findViewById(R.id.userDeleteBT);
        problemsRC = v.findViewById(R.id.problemsRC);
        permanantAddressTV = v.findViewById(R.id.permanantAddressTV);

        userDeleteBT.setOnClickListener(new View.OnClickListener() {
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

        editProfileBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileEditActivity.class);
                intent.putExtra("profile", A247.TYPE_PATIENT);
                startActivity(intent);
            }
        });

        addProblemTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                EditText editText = new EditText(context);
                builder.setView(editText);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String id = A247.getDatabaseReference().push().getKey();
                            String prob = editText.getText().toString().trim();
                            Problem problem = new Problem(id, prob);
                            A247.problems(A247.getUserId()).child(id).setValue(problem);
                        }catch (Exception e){
                            Log.d(TAG, "onClick: " + e.getMessage());
                        }

                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        A247.problems(A247.getUserId()).addValueEventListener(new ValueEventListener() {
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

        A247.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Patient profile = dataSnapshot.getValue(Patient.class);
                    try {
                        nameTV.setText(profile.getName());
                        ageTV.setText("Age : "+profile.getAge() + " Years");
                        weightTV.setText("Weight : "+profile.getWeight() +" KG");
                        phoneTV.setText("Phone : " + profile.getPhoneNumber());
                        genderTV.setText("Gender : "+profile.getGender());
                        fatherNameTV.setText("Father name : " + profile.getFatherName());
                        motherNameTV.setText("Mother Name : " + profile.getMotherName());

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

    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context_ = getActivity();
        context = context_;
    }

    private void profileDelete() {

        A247.userPhoneNumber(A247.getUser().getPhoneNumber()).removeValue();
        A247.allPatients().child(A247.getUserId()).removeValue();
        A247.problems(A247.getUserId()).removeValue();
        A247.removeUserType().removeValue();

        A247.request().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Request request = data.getValue(Request.class);
                        if (TextUtils.equals(request.getPatientid(), A247.getUserId())){
                            A247.request().child(request.getId()).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        A247.deleteUser().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        A247.getFirebaseAuth().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                try {
                    A247.getFirebaseAuth().signOut();
                    startActivity(new Intent(context, Registration.class));
                    getActivity().finish();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.d(TAG, "onSuccess: " + e.getMessage());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                try {
                    A247.getFirebaseAuth().signOut();
                    startActivity(new Intent(context, Registration.class));
                    getActivity().finish();
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }catch (Exception ee){
                    Log.d(TAG, "onFailure: "+ ee.getMessage());
                }
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ProfileDeleteAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            profileDelete();
            return null;
        }
    }



}
