package com.doctor.a247.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.a247.Drawer_Fragments.AllDoctors;
import com.doctor.a247.Drawer_Fragments.AllMedicine;
import com.doctor.a247.Drawer_Fragments.AllPatient;
import com.doctor.a247.Drawer_Fragments.AllPescribe;
import com.doctor.a247.Drawer_Fragments.AppoinmentsRequest;
import com.doctor.a247.Drawer_Fragments.Appointmented;
import com.doctor.a247.Drawer_Fragments.Appointments;
import com.doctor.a247.Drawer_Fragments.DoctorProfile;
import com.doctor.a247.Drawer_Fragments.PatientProfile;
import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.REG_MODEL.PatientSignupProfile;
import com.doctor.a247.model.Doctor;
import com.doctor.a247.model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();


    private CircleImageView userphoto;
    private TextView userName, userEmail;
    private FrameLayout drawerLayout;
    private String userId;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };



    @Override
    protected void onRestart() {
        super.onRestart();

        if (A247.getFirebaseAuth().getCurrentUser() == null){
            startActivity(new Intent(this, Registration.class));
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (A247.getFirebaseAuth().getCurrentUser() == null){
            startActivity(new Intent(this, Registration.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (A247.getFirebaseAuth().getCurrentUser() == null){
            startActivity(new Intent(this, Registration.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        if (A247.getFirebaseAuth().getCurrentUser() == null){
            startActivity(new Intent(this, Registration.class));
            finish();
        }

        try {

            Intent intent = getIntent();
            userId = intent.getStringExtra("userId");
            if (userId == null){
                userId = A247.getUserId();
            }
        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);

        drawerLayout = findViewById(R.id.drawerFragment);

        userphoto = v.findViewById(R.id.userphoto);
        userName = v.findViewById(R.id.userName);
        userEmail = v.findViewById(R.id.userEmail);
        if(A247.getUser() != null){
            A247.getUserType().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = dataSnapshot.getValue(String.class);
                    if (TextUtils.equals(type, A247.TYPE_DOCTOR) ){
                        try {
                            navigationView.inflateMenu(R.menu.activity_doctor_drawer);
                            getSupportActionBar().setTitle("All Patient");
                            getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllPatient()).commit();
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            userDetails(type);
                        }catch ( Exception e){
                            Log.d(TAG, "onDataChange: " + e.getMessage());
                        }

                    }else if (TextUtils.equals(type, A247.TYPE_PATIENT)){
                        try {
                            navigationView.inflateMenu(R.menu.activity_patient_drawer);

                            getSupportActionBar().setTitle("All Doctors");
                            getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllDoctors()).commit();
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            userDetails(type);

                        }catch (Exception e){
                            Log.d(TAG, "onDataChange: "+ e.getMessage());
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

    }

    private void userDetails(final String userType){

        A247.getUserProfileDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        if (TextUtils.equals(userType, A247.TYPE_DOCTOR) ){
                            Doctor profile = dataSnapshot.getValue(Doctor.class);
                            Picasso.get().load(profile.getImageUrl()).error(R.drawable.doctors_icon).placeholder(R.drawable.doctor_image_icon)
                                    .into(userphoto);
                            userName.setText(profile.getName());
                            userEmail.setText(profile.getPhoneNumber());
                        }else if (TextUtils.equals(userType, A247.TYPE_PATIENT)){
                            Patient profile = dataSnapshot.getValue(Patient.class);
                            userName.setText(profile.getName());
                            userEmail.setText(profile.getPhoneNumber());
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()){
//            case R.id.logout:
//                A247.getFirebaseAuth().signOut();
//                startActivity(new Intent(this, Registration.class));
//                finish();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
           switch (item.getItemId()){
               case R.id.appoinmentRequest:
                   getSupportActionBar().setTitle("Appointment Request");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AppoinmentsRequest()).commit();
                   break;
               case R.id.allPatient:
                   getSupportActionBar().setTitle("All Patients");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllPatient()).commit();
                   break;

               case R.id.appoinmented:
                   getSupportActionBar().setTitle("Enrolled");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new Appointmented()).commit();
                   break;
               case R.id.doctorProfile:
                   getSupportActionBar().setTitle("Profile");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new DoctorProfile()).commit();
                   break;
               case R.id.alldoctor:
                   getSupportActionBar().setTitle("All Doctors");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllDoctors()).commit();
                   break;
               case R.id.appoinments:
                   getSupportActionBar().setTitle("Appointments");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new Appointments()).commit();
                   break;
               case R.id.patientProfile:
                   getSupportActionBar().setTitle("Profile");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new PatientProfile()).commit();
                   break;
               case R.id.allMedicine:
                   getSupportActionBar().setTitle("All Medicine");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllMedicine()).commit();
                   break;
               case R.id.allPescribe:
                   getSupportActionBar().setTitle("All Pescribe");
                   getSupportFragmentManager().beginTransaction().replace(R.id.drawerFragment, new AllPescribe()).commit();
                   break;

           }
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }


}
