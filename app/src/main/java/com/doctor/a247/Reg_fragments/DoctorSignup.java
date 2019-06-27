package com.doctor.a247.Reg_fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.doctor.a247.R;
import com.doctor.a247.model.Address;
import com.doctor.a247.model.Doctor;
import com.doctor.a247.model.Experience;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorSignup extends Fragment implements View.OnClickListener {

        private static final String TAG = DoctorSignup.class.getSimpleName();
        private Context context;

        private Button nextBT;
        private EditText doctorNameET, ageET , permanantVillageET, permanantThanaET, permanantDivisionET,
                permanantPostNoET, permanantZilaET , motherNameET, fatherNameET, permanantHouseNoET , permanantRoadNoET,
                doctorDeg, speciality, presentPostNoET, presentDivisionET, presentThanaET, presentZilaET,
                presentVillageET, presentRoadNoET, doctorDesc, presentHouseNoET, expDepartNameET,
                expStartDateET, expEndDateET;

        private CircleImageView doctorImage;
        private TextView errorTV;
        private RadioGroup genderRG;

        private String doctorgender;

        private final int SELECT_IMAGE = 11;
        private Uri pic_uri;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public DoctorSignup() {
        // Required empty public constructor
    }


    public static DoctorSignup newInstance(String param1, String param2) {
        DoctorSignup fragment = new DoctorSignup();
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
        final View v = inflater.inflate(R.layout.fragment_doctor_signup, container, false);

        expDepartNameET = v.findViewById(R.id.expDepartNameET);
        expStartDateET = v.findViewById(R.id.expStartDateET);
        expEndDateET = v.findViewById(R.id.expEndDateET);
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
        ageET = v.findViewById(R.id.ageET);
        nextBT = v.findViewById(R.id.nextBT);
        doctorNameET = v.findViewById(R.id.doctorNameET);
        doctorDeg = v.findViewById(R.id.doctorDeg);
        speciality = v.findViewById(R.id.specility);
        doctorImage = v.findViewById(R.id.doctorImg);
        errorTV = v.findViewById(R.id.errorTV);
        genderRG = v.findViewById(R.id.genderRG);

        expEndDateET.setFocusable(false);
        expEndDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.datepicker, null);
                DatePicker datePicker = view.findViewById(R.id.datepicker);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                        expEndDateET.setText(date);
                        expEndDateET.setFocusable(true);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });

        expStartDateET.setFocusable(false);
        expStartDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.datepicker, null);
                DatePicker datePicker = view.findViewById(R.id.datepicker);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = datePicker.getDayOfMonth() + "/" + datePicker.getMonth() + "/" + datePicker.getYear();
                        expStartDateET.setText(date);
                        expStartDateET.setFocusable(true);
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });


        doctorImage.setDrawingCacheEnabled(true);
        doctorImage.buildDrawingCache();
        doctorImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()){
                    Intent pic = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pic.setType("image/*");
                    startActivityForResult(pic.createChooser(pic, "Select Image"), SELECT_IMAGE);
                }

            }
        });



        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doctorgender = ((RadioButton) v.findViewById(checkedId)).getText().toString();
            }
        });

        nextBT.setOnClickListener(this);


        return v;
    }


    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        Bundle arguments = getArguments();
        context_ = getActivity();
        String s = arguments.getString("doctor");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nextBT){
            doctor();
        }
    }

    private void doctor() {

         String name = null;
         String id = null;
         int age = 0;
         String fatherName = null;
         String motherName = null;
         String phoneNumber = null;
         Address presentAddress = null;
         Address permanantAddress = null;
         String gender = doctorgender;
         String degination = null;
         String doctorSpe = null;

         String hostpitalName = null;
         String deptName = null;
         String startDate = null;
         String endDate = null;

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
            name = doctorNameET.getText().toString().trim();
            age = Integer.parseInt(ageET.getText().toString().trim());
            degination = doctorDeg.getText().toString().trim();
            doctorSpe = speciality.getText().toString();
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

            hostpitalName = expDepartNameET.getText().toString().trim();
            deptName = expDepartNameET.getText().toString().trim();
            startDate = expStartDateET.getText().toString().trim();
            endDate = expEndDateET.getText().toString().trim();


        }catch (Exception e){
            Log.d(TAG, "doctor: " + e.getMessage());
        }




        String error = "Please complete this field";
        if (TextUtils.isEmpty(name)){
            doctorNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(degination)){
            doctorDeg.setError(error);
            return;
        }else if (TextUtils.isEmpty(doctorSpe)){
            speciality.setError(error);
            return;
        }else if (TextUtils.isEmpty(fatherName)){
            fatherNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(motherName)){
            motherNameET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentHouseNo)){
            presentHouseNoET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentRoadNo)){
            presentRoadNoET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentVillage)){
            presentVillageET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentPost)){
            presentPostNoET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentThana)){
            presentThanaET.setError(error);
            return;
        }else if (TextUtils.isEmpty(presentZila)){
            presentZilaET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantHouseNo)){
            permanantHouseNoET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantRoadNo)){
            permanantRoadNoET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantVillage)){
            permanantVillageET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantPost)){
            permanantPostNoET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantThana)){
            permanantThanaET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantZila)){
            permanantZilaET.setError(error);
            return;
        }else if (TextUtils.isEmpty(permanantDivision)){
            permanantDivisionET.setError(error);
            return;
        }else if (TextUtils.isEmpty(gender)){
            Toast.makeText(getActivity(), "Gender Not Selected", Toast.LENGTH_SHORT).show();
            errorTV.setError("Please Select Gender");
            return;
        }else if (pic_uri == null){
            Toast.makeText(getActivity(), "Profile Image Not Selected", Toast.LENGTH_SHORT).show();
            errorTV.setError("Please Select Your Image");
            return;
        }

        else if (!TextUtils.isEmpty(name)
                &&  !TextUtils.isEmpty(degination)
                &&  !TextUtils.isEmpty(doctorSpe)
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
                &&  !TextUtils.isEmpty(startDate)
                &&  !TextUtils.isEmpty(deptName)
                &&  !TextUtils.isEmpty(endDate)
                &&  !TextUtils.isEmpty(hostpitalName) && !TextUtils.isEmpty(doctorgender) && pic_uri != null){

            presentAddress = new Address( presentHouseNo, presentRoadNo, presentVillage, presentPost, presentThana, presentZila, presentDivision);
            permanantAddress = new Address(permanantHouseNo, permanantRoadNo, permanantVillage, permanantPost, permanantThana, permanantZila, permanantDivision);
            Experience experience = new Experience(hostpitalName, deptName, startDate, endDate);

            Doctor doctor = new Doctor(name, null, age, fatherName, motherName, phoneNumber, presentAddress, permanantAddress, gender,
                    experience, doctorSpe, null, degination);


            Bundle bundle = new Bundle();
            Signup signup = new Signup();
            bundle.putString("type", "Doctor");
            bundle.putParcelable("uri", pic_uri);
            bundle.putSerializable("doctor", doctor);
            signup.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.regFragmentContainer, signup);
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();

        }else {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = context = getActivity();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE){
            try {
                pic_uri = data.getData();
                doctorImage.setImageURI(data.getData());
            }catch (Exception e){
                Toast.makeText(getActivity(), "Image Not set", Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }
}
