package com.doctor.a247.Drawer_Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;


import com.doctor.a247.A247;
import com.doctor.a247.R;
import com.doctor.a247.activity.MainActivity;
import com.doctor.a247.activity.Registration;
import com.doctor.a247.adapter.AllDoctorsAdapter;
import com.doctor.a247.model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class AllDoctors extends Fragment implements SearchView.OnQueryTextListener {

    private Context context;

    private RecyclerView allDoctorRV;
    private AllDoctorsAdapter adapter;
    private ArrayList<Doctor> doctorList;

    private CardView notDoctorCV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_all_doctors, container, false);
        notDoctorCV = v.findViewById(R.id.notDoctorCV);
        allDoctorRV = v.findViewById(R.id.allDoctorRV);

//        LinearLayoutManager llm = new LinearLayoutManager(context);
//        llm.setOrientation(LinearLayoutManager.VERTICAL);
//        alldoctor.setLayoutManager(llm);

        A247.allDoctors().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null){
                    doctorList = new ArrayList<Doctor>();
                    for (DataSnapshot childrenShot : dataSnapshot.getChildren()){
                        Doctor  doctor = childrenShot.getValue(Doctor.class);
                        doctorList.add(doctor);
                    }

                    if (doctorList.size() <= 0){
                        notDoctorCV.setVisibility(View.VISIBLE);
                        allDoctorRV.setVisibility(View.GONE);
                    }else if (doctorList.size() > 0){
                        adapter = new AllDoctorsAdapter(context, doctorList);
                        allDoctorRV.setVisibility(View.VISIBLE);
                        allDoctorRV.setAdapter(adapter);
                        notDoctorCV.setVisibility(View.GONE);

                    }

                }
                if (dataSnapshot == null){
                    View parentLayout = v.findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, "Error...", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_option_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(this);
        sv.setIconifiedByDefault(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                startActivity(new Intent(context, Registration.class));
                A247.getFirebaseAuth().signOut();
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            adapter.getFilter().filter(s);
        }catch (Exception e){
            Log.d("TAG", "onQueryTextChange: " + e.getMessage());
        }
        return false;
    }
}
