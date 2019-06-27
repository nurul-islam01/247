package com.doctor.a247.edit;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.Reg_fragments.PatientSignup;
import com.doctor.a247.model.Address;
import com.doctor.a247.model.Patient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientProfileEdit extends Fragment {

    private static final String TAG = PatientProfileEdit.class.getSimpleName();
    private Context context;
    private EditText pNameET, pAgeET, pWeghitET;
    private EditText  permanantVillageET, permanantThanaET, permanantDivisionET,
            permanantPostNoET, permanantZilaET , motherNameET, fatherNameET, permanantHouseNoET , permanantRoadNoET,
            presentPostNoET, presentDivisionET, presentThanaET, presentZilaET,
            presentVillageET, presentRoadNoET, presentHouseNoET;

    private Spinner bloodGroupSp;
    private RadioGroup genderRG;
    private RadioButton maleRB, femaleRB;
    private Button submit;
    private String gender;

    private ProfileUpdateAsync async;

    public PatientProfileEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v =  inflater.inflate(R.layout.fragment_patient_profile_edit, container, false);

        pNameET = v.findViewById(R.id.pNameET);
        pAgeET = v.findViewById(R.id.pAgeET);
        pWeghitET = v.findViewById(R.id.pWeghitET);
        bloodGroupSp = v.findViewById(R.id.bloodGroupSp);
        presentHouseNoET = v.findViewById(R.id.presentHouseNoET);
        presentRoadNoET = v.findViewById(R.id.presentRoadNoET);
        presentVillageET = v.findViewById(R.id.presentVillageET);
        presentZilaET = v.findViewById(R.id.presentZilaET);
        presentThanaET = v.findViewById(R.id.presentThanaET);
        presentDivisionET = v.findViewById(R.id.presentDivisionET);
        presentPostNoET = v.findViewById(R.id.presentPostNoET);
        permanantRoadNoET = v.findViewById(R.id.permanantRoadNoET);
        permanantHouseNoET = v.findViewById(R.id.permanantHouseNoET);
        fatherNameET = v.findViewById(R.id.fatherNameET);
        motherNameET = v.findViewById(R.id.motherNameET);
        permanantZilaET = v.findViewById(R.id.permanantZilaET);
        permanantPostNoET = v.findViewById(R.id.permanantPostNoET);
        permanantDivisionET = v.findViewById(R.id.permanantDivisionET);
        permanantThanaET = v.findViewById(R.id.permanantThanaET);
        permanantVillageET = v.findViewById(R.id.permanantVillageET);
        maleRB = v.findViewById(R.id.maleRB);
        femaleRB = v.findViewById(R.id.femaleRB);

        submit = v.findViewById(R.id.submit);
        genderRG = v.findViewById(R.id.genderRG);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                async = new ProfileUpdateAsync(getActivity());
                async.execute();
            }
        });

        A247.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    Patient profile = dataSnapshot.getValue(Patient.class);
//                    try {

                        pNameET.setText(profile.getName());
                        pAgeET.setText(String.valueOf(profile.getAge()));
                        pWeghitET.setText(String.valueOf(profile.getWeight()));

                        permanantVillageET.setText(profile.getPresentAddress().getVillage());
                        permanantThanaET.setText(profile.getPresentAddress().getThana());
                        permanantDivisionET.setText(profile.getPresentAddress().getDivision());
                        permanantPostNoET.setText(profile.getPermanantAddress().getPost());
                        permanantZilaET.setText(profile.getPermanantAddress().getZila());
                        motherNameET.setText(profile.getMotherName());
                        fatherNameET.setText(profile.getFatherName());
                        permanantHouseNoET.setText(profile.getPermanantAddress().getHouseNo());
                        permanantRoadNoET.setText(profile.getPermanantAddress().getRoadNo());
                        presentPostNoET.setText(profile.getPresentAddress().getPost());
                        presentDivisionET.setText(profile.getPresentAddress().getDivision());
                        presentThanaET.setText(profile.getPresentAddress().getThana());
                        presentZilaET.setText(profile.getPresentAddress().getZila());
                        presentVillageET.setText(profile.getPresentAddress().getVillage());
                        presentRoadNoET.setText(profile.getPresentAddress().getRoadNo());
                        presentHouseNoET.setText(profile.getPresentAddress().getHouseNo());

                        if (TextUtils.equals(profile.getGender(), "Male")){
                            maleRB.setChecked(true);
                        }else if (TextUtils.equals(profile.getGender(), "Female")){
                            femaleRB.setChecked(true);
                        }
//
//                    }catch (Exception e){
//                        Log.d(TAG, "onDataChange: " + e.getMessage());
//                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = ((RadioButton) v.findViewById(checkedId)).getText().toString();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

    private void updateProfile(){

        String bloodGroup = null;
        int weight = 0;
        String name = null;
        String id = A247.getUserId();
        int age = 0;
        String fatherName = null;
        String motherName = null;
        String phoneNumber = A247.getUser().getPhoneNumber();
        Address presentAddress = null;
        Address permanantAddress = null;
        String gender = this.gender;


        String presentHouseNo = null;
        String presentRoadNo = null;
        String presentVillage = null;
        String presentPost = null;
        String presentThana = null;
        String presentZila = null;
        String presentDivision = null;

        String permanantHouseNo = null;
        String permanantRoadNo = null;
        String permanantVillage = null;
        String permanantPost = null;
        String permanantThana = null;
        String permanantZila = null;
        String permanantDivision = null;
        try {

            name = pNameET.getText().toString().trim();
            age = Integer.parseInt(pAgeET.getText().toString().trim());
            weight = Integer.parseInt(pWeghitET.getText().toString().trim());
            bloodGroup = bloodGroupSp.getSelectedItem().toString().trim();

            fatherName = fatherNameET.getText().toString().trim();
            motherName = motherNameET.getText().toString().trim();

            presentHouseNo = presentHouseNoET.getText().toString().trim();
            presentRoadNo = presentRoadNoET.getText().toString().trim();
            presentVillage = presentVillageET.getText().toString().trim();
            presentPost = presentPostNoET.getText().toString().trim();
            presentThana = presentThanaET.getText().toString().trim();
            presentZila = presentZilaET.getText().toString().trim();
            presentDivision = presentDivisionET.getText().toString().trim();
            permanantHouseNo = permanantHouseNoET.getText().toString().trim();
            permanantRoadNo = permanantRoadNoET.getText().toString().trim();
            permanantVillage = permanantVillageET.getText().toString().trim();
            permanantPost = permanantPostNoET.getText().toString().trim();
            permanantThana = permanantThanaET.getText().toString().trim();
            permanantZila = permanantZilaET.getText().toString().trim();
            permanantDivision = permanantDivisionET.getText().toString().trim();


        }catch (Exception e){
            Log.d("TAG", "patientSignup: " + e.getMessage());
        }

        String error = "Complete This Field";

        if (TextUtils.isEmpty(name)){
            pNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(fatherName)){
            fatherNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(this.gender)){
            Toast.makeText(context, "Gender Is not set", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(motherName)){
            motherNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentVillage)){
            presentVillageET.setError(error);
            return;
        }else if (age > 120){
            pAgeET.setError("Not Perfect");
            return;
        } else if (TextUtils.isEmpty(presentHouseNo)){
            presentHouseNoET.setError(error);
        }else if (weight > 200){
            pWeghitET.setError("Not Perfect");
            return;
        }else if (TextUtils.isEmpty(permanantDivision)){
            permanantDivisionET.setError(error);
            return;
        }else if (TextUtils.isEmpty(gender)){
            Toast.makeText(context, "Select Gender", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(bloodGroup)){
            Toast.makeText(context, "Set Blood Group", Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isEmpty(name)
                &&  !TextUtils.isEmpty(gender)
                &&  !TextUtils.isEmpty(fatherName)
                &&  !TextUtils.isEmpty(motherName)

                &&  !TextUtils.isEmpty(presentHouseNo)
                &&  !TextUtils.isEmpty(presentRoadNo)
                &&  !TextUtils.isEmpty(presentVillage)
                &&  !TextUtils.isEmpty(presentPost)
                &&  !TextUtils.isEmpty(presentThana)
                &&  !TextUtils.isEmpty(presentZila)
                &&  !TextUtils.isEmpty(presentDivision)
                &&  !TextUtils.isEmpty(permanantHouseNo)
                &&  !TextUtils.isEmpty(permanantRoadNo)
                &&  !TextUtils.isEmpty(permanantVillage)
                &&  !TextUtils.isEmpty(permanantPost)
                &&  !TextUtils.isEmpty(permanantThana)
                &&  !TextUtils.isEmpty(permanantZila)
                &&  !TextUtils.isEmpty(permanantDivision)
                &&  age != 0
                &&  weight != 0
                && !TextUtils.isEmpty(gender)
                && !TextUtils.isEmpty(bloodGroup)){

            presentAddress = new Address(presentHouseNo, presentRoadNo, presentVillage, presentPost, presentThana, presentZila, presentDivision);
            permanantAddress = new Address(permanantHouseNo, permanantRoadNo, permanantVillage, permanantPost, permanantThana, permanantZila, permanantDivision);
            Patient patient = new Patient(name, id, age, fatherName, motherName, phoneNumber, presentAddress, permanantAddress, gender, bloodGroup, weight);

            A247.getUserProfileDatabaseReference().setValue(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
            A247.allPatients().child(A247.getUserId()).setValue(patient);
        }else {
            Toast.makeText(context, "Empty Field", Toast.LENGTH_SHORT).show();
        }
    }

    private class ProfileUpdateAsync extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        public ProfileUpdateAsync(Context context) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Updating...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            updateProfile();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }
}
