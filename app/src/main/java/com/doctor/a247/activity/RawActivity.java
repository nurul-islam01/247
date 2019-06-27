package com.doctor.a247.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.Raw_Activity_Fragments.Doctor_profile_for_appointment;
import com.doctor.a247.Raw_Activity_Fragments.PatientProfileForDoctor;


public class RawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw);

        Intent intent = getIntent();
        String getFragment = intent.getStringExtra("fragment");

        if (getFragment.equals("doctorProfile")){
            getSupportActionBar().setTitle("Doctor Details");
            String doctorId = intent.getStringExtra("doctorId");
            Bundle bundle = new Bundle();
            bundle.putString("doctorId", doctorId);

            Doctor_profile_for_appointment appoinment = new Doctor_profile_for_appointment();

            appoinment.setArguments(bundle);

            FragmentManager fragmentManager = RawActivity.this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.raw_frament_container, appoinment);
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();
        }else if (getFragment.equals("patientProfile")){
            getSupportActionBar().setTitle("Doctor Details");
            String patientId = intent.getStringExtra("patientId");
            Bundle bundle = new Bundle();
            bundle.putString("patientId", patientId);

            PatientProfileForDoctor patientProfileForDoctor = new PatientProfileForDoctor();

            patientProfileForDoctor.setArguments(bundle);

            FragmentManager fragmentManager = RawActivity.this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.raw_frament_container, patientProfileForDoctor);
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                A247.getFirebaseAuth().signOut();
                startActivity(new Intent(this, Registration.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
