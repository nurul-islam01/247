package com.doctor.a247.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.doctor.a247.A247;
import com.doctor.a247.Patient_Fragments.PProfile;
import com.doctor.a247.Patient_Fragments.PatientMedicines;
import com.doctor.a247.Patient_Fragments.PatientPescriptions;
import com.doctor.a247.R;
import com.doctor.a247.SingleAppoinment.ApDoctorProfile;
import com.doctor.a247.SingleAppoinment.DoctorMedicine;
import com.doctor.a247.SingleAppoinment.DoctorPescription;
import com.doctor.a247.model.Appoint;


public class SingleAppointment extends AppCompatActivity {

    private static final String TAG = SingleAppointment.class.getSimpleName();

    private static Appoint appoint;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter pagerAdapter;

    public static Appoint getAppoint() {
        return appoint;
    }

    private AppoinmenttDoctor appoinmenttDoctor;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_appointment);
        getSupportActionBar().setTitle("Appointment description");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Medicines"));
        tabLayout.addTab(tabLayout.newTab().setText("Pescriptions"));


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Intent intent = getIntent();
        appoint = (Appoint) intent.getSerializableExtra("row");


    }





    public interface AppoinmenttDoctor {
        public void appoinment(String appoinmentId);
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
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.logout:
                startActivity(new Intent(this, Registration.class));
                A247.getFirebaseAuth().signOut();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private class ViewPagerAdapter extends FragmentPagerAdapter {
        int tabCount;
        public ViewPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int i) {

            switch (i){
                case 0:
                    return new ApDoctorProfile();
                case 1:
                    return new DoctorMedicine();
                case 2:
                    return new DoctorPescription();
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
