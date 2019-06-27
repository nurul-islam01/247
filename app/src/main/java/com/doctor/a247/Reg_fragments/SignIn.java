package com.doctor.a247.Reg_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.activity.MainActivity;
import com.doctor.a247.model.FirebaseToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hbb20.CountryCodePicker;


import java.util.concurrent.TimeUnit;

public class SignIn extends Fragment implements View.OnClickListener {

    private static final String TAG = SignIn.class.getSimpleName();
    private Context context;

    private FirebaseAuth auth;

    private TextView newAccountTV, errorShowTV;
    private Button singinBT;
    private LinearLayout codeSentPanel, verifyPanel;

    private ProgressBar progressBar;

    private Button signupBT;
    private LinearLayout verifyBTS;
    private EditText  phoneET,codeET ;
    private Button verifyBT, resendCodeBT;
    private TextInputLayout phoneTIL, codeTIL;
    private String verificationId;
    private String phone;
    private CountryCodePicker countryCodePicker;

    private boolean numberValide;


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        auth = FirebaseAuth.getInstance();

        codeSentPanel = v.findViewById(R.id.codeSentPanel);
        verifyPanel = v.findViewById(R.id.verifyPanel);
        newAccountTV = v.findViewById(R.id.newAccountTV);
        errorShowTV = v.findViewById(R.id.errorshowTV);

        progressBar = v.findViewById(R.id.progress_bar);

        phoneET = v.findViewById(R.id.phoneET);
        codeET = v.findViewById(R.id.codeET);
        signupBT = v.findViewById(R.id.signupBT);
        progressBar = v.findViewById(R.id.progress_bar);
        countryCodePicker =  (CountryCodePicker) v.findViewById(R.id.ccpsignin);

        phoneTIL = v.findViewById(R.id.phoneTIL);
        codeTIL = v.findViewById(R.id.codeTIL);
        verifyBT = v.findViewById(R.id.verifyBT);
        resendCodeBT = v.findViewById(R.id.resendCodeBT);
        verifyBTS = v.findViewById(R.id.verifyBTS);

        countryCodePicker.registerCarrierNumberEditText(phoneET);
        codeSentPanel.setVisibility(View.VISIBLE);
        verifyPanel.setVisibility(View.GONE);

        signupBT.setOnClickListener(this);
        resendCodeBT.setOnClickListener(this);
        verifyBT.setOnClickListener(this);
        newAccountTV.setOnClickListener(this);
        signupBT.setOnClickListener(this);

        countryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber){
                    numberValide = isValidNumber;
                    phone = countryCodePicker.getFullNumberWithPlus();
                }else {
                    numberValide = isValidNumber;
                }
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getToken() != null){
                        Log.d(TAG, "token: " + task.getResult().getToken());
                    }

                }
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                    Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.signupBT:
                progressBar.setVisibility(View.VISIBLE);
                isSignup();
                break;
            case R.id.resendCodeBT:
                if (verifyBTS.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                    codeSentPanel.setVisibility(View.VISIBLE);
                    verifyPanel.setVisibility(View.GONE);
                }
            case R.id.newAccountTV:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null).replace(R.id.regFragmentContainer, new Identifier());
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.commit();
                break;
        }
    }

    private void isSignup(){

           if (numberValide && phone != null) {
               A247.userPhoneNumber(phone).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if (dataSnapshot.getValue(String.class) != null ) {
                           sendVerificationCode();
                       } else {
                           Toast.makeText(context, "Not Register", Toast.LENGTH_SHORT).show();
                           errorShowTV.setText("Register First");
                           progressBar.setVisibility(View.GONE);
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       errorShowTV.setText("Register First");
                       progressBar.setVisibility(View.GONE);
                   }
               });
           }else {
               Toast.makeText(context, "Invalide Number", Toast.LENGTH_SHORT).show();
               errorShowTV.setText("Invalide Number");
           }

    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signinWithCredential(credential);
    }

    private void signinWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().getToken() != null){
                                            FirebaseToken token = new FirebaseToken(task.getResult().getToken(), auth.getUid());
                                            A247.token().child(auth.getUid()).setValue(token);
                                        }

                                    }
                                }
                            });


                            Toast.makeText(context, "Login", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("userId", auth.getUid());
                            context.startActivity(intent);
                            getActivity().finish();

                        }else {
                            Toast.makeText(getActivity(), "Verification Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Verification Failed", Toast.LENGTH_SHORT).show();
                errorShowTV.setTag("Failed in sign in");
            }
        });
    }


    private void sendVerificationCode(){
        try {
            if (numberValide){
                phone = countryCodePicker.getFullNumberWithPlus();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phone,
                        60,
                        TimeUnit.SECONDS,
                        TaskExecutors.MAIN_THREAD,
                        mCallback
                );
            }else if (phone.isEmpty()){
                Toast.makeText(context, "Number Empty", Toast.LENGTH_SHORT).show();
                return;
            }else {
                Toast.makeText(context, "Invalide Number", Toast.LENGTH_SHORT).show();
                return;
            }

        }catch (Exception e){
            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "sendVerificationCode: "+e.getMessage());
            errorShowTV.setTag("Error Occred");
        }

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Toast.makeText(getActivity(), "Code Sent", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Verification Sent Failed", Toast.LENGTH_SHORT).show();
            errorShowTV.setTag("Verification Send Failed");
        }
    };


}
