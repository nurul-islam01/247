package com.doctor.a247.edit;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.model.Address;
import com.doctor.a247.model.Doctor;
import com.doctor.a247.model.Experience;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DoctorProfileEdit extends Fragment {

    private static final String TAG = DoctorProfileEdit.class.getSimpleName();
    private Context context;

    private CircleImageView doctorImageCIV;
    private EditText doctorNameET, ageET , permanantVillageET, permanantThanaET, permanantDivisionET,
            permanantPostNoET, permanantZilaET , motherNameET, fatherNameET, permanantHouseNoET , permanantRoadNoET,
            doctorDeg, speciality, presentPostNoET, presentDivisionET, presentThanaET, presentZilaET,
            presentVillageET, presentRoadNoET, presentHouseNoET, expDepartNameET,
            expStartDateET, expEndDateET;
    private RadioGroup genderRG;
    private RadioButton maleRB, femaleRB;
    private Button submit;
    private ProfileUpdateAsync async;
    private StorageReference storageReference;


    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private final int SELECT_IMAGE = 11;
    private Uri pic_uri;
    private String doctorgender;




    public DoctorProfileEdit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_doctor_profile_edit, container, false);

        doctorImageCIV = v.findViewById(R.id.doctorImageCIV);
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
        doctorNameET = v.findViewById(R.id.doctorNameET);
        doctorDeg = v.findViewById(R.id.doctorDeg);
        speciality = v.findViewById(R.id.specility);
        genderRG = v.findViewById(R.id.genderRG);
        maleRB = v.findViewById(R.id.maleRB);
        femaleRB = v.findViewById(R.id.femaleRB);
        submit = v.findViewById(R.id.submit);



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



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
                async = new ProfileUpdateAsync();
                async.execute();
            }
        });

        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doctorgender = ((RadioButton) v.findViewById(checkedId)).getText().toString();
            }
        });


        A247.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        Doctor profile = dataSnapshot.getValue(Doctor.class);
                        Picasso.get().load(profile.getImageUrl()).error(R.drawable.doctor_image_icon)
                                .placeholder(R.drawable.doctor_image_icon)
                                .into(doctorImageCIV);
                        doctorNameET.setText(profile.getName());
                        ageET.setText(String.valueOf(profile.getAge()));
                        doctorDeg.setText(profile.getDegination());
                         permanantVillageET.setText(profile.getPresentAddress().getVillage());
                         permanantThanaET.setText(profile.getPresentAddress().getThana());
                         permanantDivisionET.setText(profile.getPresentAddress().getDivision());
                         permanantPostNoET.setText(profile.getPermanantAddress().getPost());
                         permanantZilaET.setText(profile.getPermanantAddress().getZila());
                         motherNameET.setText(profile.getMotherName());
                         fatherNameET.setText(profile.getFatherName());
                         permanantHouseNoET.setText(profile.getPermanantAddress().getHouseNo());
                         permanantRoadNoET.setText(profile.getPermanantAddress().getRoadNo());
                         speciality.setText(profile.getSpecalist());
                         presentPostNoET.setText(profile.getPresentAddress().getPost());
                         presentDivisionET.setText(profile.getPresentAddress().getDivision());
                         presentThanaET.setText(profile.getPresentAddress().getThana());
                         presentZilaET.setText(profile.getPresentAddress().getZila());
                         presentVillageET.setText(profile.getPresentAddress().getVillage());
                         presentRoadNoET.setText(profile.getPresentAddress().getRoadNo());
                         presentHouseNoET.setText(profile.getPresentAddress().getHouseNo());
                         expDepartNameET.setText(profile.getExperience().getDeptName());
                         expStartDateET.setText(profile.getExperience().getStartDate());
                         expEndDateET.setText(profile.getExperience().getEndDate());

                        if (TextUtils.equals(profile.getGender(), "Male")){
                            maleRB.setChecked(true);
                        }else if (TextUtils.equals(profile.getGender(), "Female")){
                            femaleRB.setChecked(true);
                        }

                        doctorImageCIV.setTag(profile.getImageUrl());
                    }catch (Exception e){
                        Log.d(TAG, "onDataChange: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        doctorImageCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()){
                    Intent pic = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pic.setType("image/*");
                    startActivityForResult(pic.createChooser(pic, "Select Image"), SELECT_IMAGE);
                }

            }
        });
        return v;
    }

    private void setUser() {

        String name = null;
        String id = A247.getUserId();
        int age = 0;
        String fatherName = null;
        String motherName = null;
        String phoneNumber = A247.getUser().getPhoneNumber();
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

        if (!TextUtils.isEmpty(name)
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
                &&  age > 0
                &&  !TextUtils.isEmpty(startDate)
                &&  !TextUtils.isEmpty(deptName)
                &&  !TextUtils.isEmpty(endDate)
                &&  !TextUtils.isEmpty(hostpitalName)
                &&  !TextUtils.isEmpty(doctorgender)) {

            presentAddress = new Address(presentHouseNo, presentRoadNo, presentVillage, presentPost, presentThana, presentZila, presentDivision);
            permanantAddress = new Address(permanantHouseNo, permanantRoadNo, permanantVillage, permanantPost, permanantThana, permanantZila, permanantDivision);
            Experience experience = new Experience(hostpitalName, deptName, startDate, endDate);

            Doctor doctor = new Doctor(name, id, age, fatherName, motherName, phoneNumber, presentAddress, permanantAddress, gender,
                    experience, doctorSpe, null, degination);
            if (pic_uri == null) {
                doctor.setImageUrl(doctorImageCIV.getTag().toString());
                A247.getUserProfileDatabaseReference().setValue(doctor);
                A247.allDoctors().child(A247.getUserId()).setValue(doctor);
            } else {
                withPicUpdate(doctor);
            }
        }else {
            Toast.makeText(context, "Empty some field", Toast.LENGTH_SHORT).show();
        }


    }

    private void withPicUpdate(final Doctor profile){

        CharSequence s = DateFormat.format("MM-dd-yy-hh-mm", new Date().getTime());
        storageReference = A247.doctorProfilePicStore().child(s+pic_uri.getLastPathSegment()+"."+getFileExtension(pic_uri));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pic_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] dat = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(dat);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                profile.setImageUrl(task.getResult().toString());
                A247.getUserProfileDatabaseReference().setValue(profile);
                A247.allDoctors().child(A247.getUserId()).setValue(profile);
                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE){
            try {
                pic_uri = data.getData();
                doctorImageCIV.setImageURI(pic_uri);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime =  MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private class ProfileUpdateAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            setUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
