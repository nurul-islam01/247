package com.doctor.a247.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.edit.DoctorProfileEdit;
import com.doctor.a247.edit.PatientProfileEdit;

public class ProfileEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        String profile = getIntent().getStringExtra("profile");
        Fragment fragment = null;
        if (profile.equals(A247.TYPE_DOCTOR)){
            fragment = new DoctorProfileEdit();
        }else if (profile.equals(A247.TYPE_PATIENT)){
            fragment = new PatientProfileEdit();
        }

        getSupportActionBar().setTitle("Edit Profile");
        getSupportFragmentManager().beginTransaction().replace(R.id.profileEditContainer, fragment).commit();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }
}
