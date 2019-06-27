package com.doctor.a247.Reg_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.model.Address;
import com.doctor.a247.model.Patient;

import org.w3c.dom.Text;

import java.io.Serializable;

public class PatientSignup extends Fragment {

    private Context context;

    private EditText pNameET, pAgeET, pWeghitET;

    private EditText  permanantVillageET, permanantThanaET, permanantDivisionET,
            permanantPostNoET, permanantZilaET , motherNameET, fatherNameET, permanantHouseNoET , permanantRoadNoET,
            presentPostNoET, presentDivisionET, presentThanaET, presentZilaET,
            presentVillageET, presentRoadNoET, presentHouseNoET;

    private RadioGroup pGenderRG;
    private Spinner bloodGroupSp;
    private Button nextBT;

    private String type;
    private String gender;

    public PatientSignup() {
        // Required empty public constructor
    }


    public static PatientSignup newInstance(String param1, String param2) {
        PatientSignup fragment = new PatientSignup();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_patient_signup, container, false);

        pNameET = v.findViewById(R.id.pNameET);
        pAgeET = v.findViewById(R.id.pAgeET);
        pWeghitET = v.findViewById(R.id.pWeghitET);
        pGenderRG = v.findViewById(R.id.pGenderRG);
        nextBT = v.findViewById(R.id.nextBT);
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

        pGenderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = ((RadioButton)v.findViewById(checkedId)).getText().toString();
            }
        });

        nextBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientSignup();
            }
        });



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context = context_;
        Bundle patient =getArguments();
        type = patient.getString("patient");
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void patientSignup(){
        String bloodGroup = null;
        int weight = 0;
        String name = null;
        String id = null;
        int age = 0;
        String fatherName = null;
        String motherName = null;
        String phoneNumber = null;
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
        } else if (TextUtils.isEmpty(type)){
            Toast.makeText(context, "Typed Empty", Toast.LENGTH_SHORT).show();
            return;
        }else if (!TextUtils.isEmpty(name)
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

            Bundle bundle = new Bundle();

            bundle.putString("type", "Patient");
            bundle.putSerializable("patient", patient);

            Signup signup = new Signup();

            signup.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.regFragmentContainer, signup);
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();

        }else {
            Toast.makeText(context, "Some Field Not Set", Toast.LENGTH_SHORT).show();
        }
    }

}
