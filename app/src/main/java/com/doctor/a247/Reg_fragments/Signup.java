package com.doctor.a247.Reg_fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.activity.MainActivity;
import com.doctor.a247.model.Doctor;
import com.doctor.a247.model.FirebaseToken;
import com.doctor.a247.model.Patient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Signup extends Fragment implements View.OnClickListener {

    private static final String TAG = Signup.class.getSimpleName();

    private static Context context;
    private LinearLayout codeSentPanel, verifyPanel;
    private TextView errorshowTV;
    private EditText  phoneET,codeET;
    private ImageView pictureDoctor;
    private Button signupBT;
    private ProgressBar progressBar;
    private LinearLayout verifyBTS;
    private Button verifyBT, resendCodeBT;
    private TextInputLayout phoneTIL, codeTIL;
    private CountryCodePicker countryCodePicker;

    private StorageReference storageReference;


    private Doctor doctor;
    private Patient patient;
    private PatientSignupProfile patientSignupProfile;
    private String usertype;
    private Uri filePic;

    private String verificationId;
    private String phone;
    private boolean numberValide;

    public Signup() {
        // Required empty public constructor
    }


    public static Signup newInstance(Context context_) {
        Signup fragment = new Signup();
        context = context_;
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
        final View v =inflater.inflate(R.layout.fragment_signup, container, false);

        codeSentPanel = v.findViewById(R.id.codeSentPanel);
        verifyPanel = v.findViewById(R.id.verifyPanel);
        verifyPanel.setVisibility(View.GONE);
        phoneET = v.findViewById(R.id.phoneET);
        codeET = v.findViewById(R.id.codeET);
        signupBT = v.findViewById(R.id.signupBT);
        errorshowTV = v.findViewById(R.id.errorshowTV);
        progressBar = v.findViewById(R.id.progress_bar);
        pictureDoctor = v.findViewById(R.id.pictureDoctor);
        countryCodePicker = v.findViewById(R.id.ccp);

        phoneTIL = v.findViewById(R.id.phoneTIL);
        codeTIL = v.findViewById(R.id.codeTIL);
        verifyBT = v.findViewById(R.id.verifyBT);
        resendCodeBT = v.findViewById(R.id.resendCodeBT);
        verifyBTS = v.findViewById(R.id.verifyBTS);

        countryCodePicker.registerCarrierNumberEditText(phoneET);
        signupBT.setOnClickListener(this);
        resendCodeBT.setOnClickListener(this);
        verifyBT.setOnClickListener(this);

        countryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber){
                    numberValide = isValidNumber;
                    phone = countryCodePicker.getFullNumberWithPlus();
                } else{
                    numberValide = isValidNumber;
                }
            }
        });

        if (filePic != null){
            pictureDoctor.setVisibility(View.VISIBLE);
            pictureDoctor.setImageURI(filePic);

        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.verifyBT:
                try {
                    final String code = codeET.getText().toString().trim();
                    if (TextUtils.isEmpty(code)){
                        Toast.makeText(context, "Code Is empty", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }
                }catch (Exception e){
                    Log.d(TAG, "onClick: " + e.getMessage());
                }

                break;

            case R.id.signupBT:
                progressBar.setVisibility(View.VISIBLE);
                isSignup();
                break;

            case R.id.resendCodeBT:
                if (verifyBTS.getVisibility() == View.VISIBLE){
                    codeSentPanel.setVisibility(View.VISIBLE);
                    verifyPanel.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
        }
    }

    private void isSignup(){
        if (numberValide && phone != null) {
            phone = countryCodePicker.getFullNumberWithPlus();
            A247.userPhoneNumber(phone).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Toast.makeText(context, "Already Register", Toast.LENGTH_SHORT).show();
                        errorshowTV.setText("Already Registered Please Login");
                        return;
                    } else {
                        sendVerificationCode(phone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    sendVerificationCode(phone);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
        Bundle bundle = getArguments();

        usertype = bundle.getString("type");

        if (usertype.equals("Doctor")){
            doctor = (Doctor) bundle.getSerializable("doctor");
            filePic = bundle.getParcelable("uri");
        }else if (usertype.equals("Patient")){
            patient = (Patient) bundle.getSerializable("patient");
        }
    }



    private void verifyCode(String code){
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signinWithCredential(credential);
    }

    private void signinWithCredential(PhoneAuthCredential credential) {
        A247.getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           new SignupAsync().execute();
                        }else {
                            Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getException());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendVerificationCode(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Toast.makeText(context, "Code Sent", Toast.LENGTH_SHORT).show();
            codeSentPanel.setVisibility(View.GONE);
            verifyPanel.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {

                try {
                    codeET.setText(code);
                    progressBar.setVisibility(View.VISIBLE);
                    String mCode = codeET.getText().toString().trim();
                    verifyCode(mCode);
                }catch (Exception e){
                    Log.d(TAG, "onVerificationCompleted: " + e.getMessage());
                }

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Verification send Failed", Toast.LENGTH_SHORT).show();
        }
    };

    private void patientSignup() {
       progressBar.setVisibility(View.VISIBLE);
       patient.setPhoneNumber(phone);
       patient.setId(A247.getUserId());
        A247.getUserType().setValue(usertype);
        A247.userPhoneNumber(phone).setValue(phone);
        A247.allPatients().child(A247.getUserId()).setValue(patient);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getToken() != null){
                        FirebaseToken token = new FirebaseToken(task.getResult().getToken(), A247.getUserId());
                        A247.token().child(A247.getUserId()).setValue(token);
                    }

                }
            }
        });

        A247.getUserProfileDatabaseReference().setValue(patient)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){


                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("userId", A247.getUserId());
                            context.startActivity(intent);
                            getActivity().finish();
                        }else {
                            Log.d(TAG, "onComplete: "+task.getResult());
                            Toast.makeText(context, "User Save Failed", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
                Toast.makeText(context, "User Save Failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
        });
        progressBar.setVisibility(View.GONE);


    }

    private void doctorSignup() {

        CharSequence s  = DateFormat.format("MM-dd-yy-hh-mm", new Date().getTime());

        storageReference = A247.doctorProfilePicStore().child(s+filePic.getLastPathSegment()+"."+getFileExtension(filePic));
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getToken() != null){
                        FirebaseToken token = new FirebaseToken(task.getResult().getToken(), A247.getUserId());
                        A247.token().child(A247.getUserId()).setValue(token);
                    }

                }
            }
        });

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePic);
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
                if (!task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    doctor.setPhoneNumber(phone);
                    doctor.setImageUrl(task.getResult().toString());
                    doctor.setId(A247.getUserId());

                    A247.getUserProfileDatabaseReference().setValue(doctor);
                    A247.getUserType().setValue(usertype);
                    A247.userPhoneNumber(phone).setValue(phone);
                    progressBar.setVisibility(View.GONE);

                    A247.allDoctors().child(A247.getUserId()).setValue(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("userId", A247.getUserId());
                            context.startActivity(intent);
                            getActivity().finish();
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(context, "Complete Picture Uploading", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onComplete: " + task.getException());
                    Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show();
                    errorshowTV.setText("Failed Signup");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "onComplete: " + e.getLocalizedMessage());
                Toast.makeText(context, "Signup failed", Toast.LENGTH_SHORT).show();
                errorshowTV.setText("Failed Signup");
            }
        });

    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime =  MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private class SignupAsync extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Loading....", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (usertype.equals("Doctor")){
                doctorSignup();
            }else if (usertype.equals("Patient")){
                patientSignup();
            }
            return null;
        }
    }

}
