package com.doctor.a247.activity;


import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.doctor.a247.Patient_Fragments.PProfile;
import com.doctor.a247.Patient_Fragments.PatientMedicines;
import com.doctor.a247.Patient_Fragments.PatientPescriptions;
import com.doctor.a247.R;
import com.doctor.a247.model.Appoint;


public class PatientDetails extends AppCompatActivity {

    private static final String TAG = PatientDetails.class.getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ViewPagerAdapter pagerAdapter;
    private static Appoint appoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        getSupportActionBar().setTitle("Patient Details");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        try{
            appoint = (Appoint) intent.getSerializableExtra("patientId");
        }catch (Exception e){
            Log.d(TAG, "onCreate: "+e.getMessage());
        }

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

        viewPager.setOffscreenPageLimit(2);

    }

    public static Appoint getAppoint(){
        return appoint;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                finish();
                return true;
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
                    return new PProfile();
                case 1:
                    return new PatientMedicines();
                case 2:
                    return new PatientPescriptions();
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
