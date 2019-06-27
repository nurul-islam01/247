package com.doctor.a247.Drawer_Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
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
import com.doctor.a247.adapter.AppointmentsAdapter;
import com.doctor.a247.model.Appoint;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class Appointments extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = Appointments.class.getSimpleName();

    private Context context;
    private ArrayList<Appoint> appoinmentList = new ArrayList<Appoint>();

    private CardView noAppoinmentCV;
    private RecyclerView appoinmentsRV;
    private AppointmentsAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appointments, container, false);

        appoinmentsRV = v.findViewById(R.id.appoinmentsRV);
        noAppoinmentCV = v.findViewById(R.id.noAppoinmentCV);

        A247.appointed().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    appoinmentList = new ArrayList<Appoint>();
                    for (DataSnapshot d : dataSnapshot.getChildren()){
                        Appoint appoint = d.getValue(Appoint.class);
                        if (TextUtils.equals(appoint.getPatientid(), A247.getUserId())){
                            appoinmentList.add(appoint);
                        }
                    }

                    if (appoinmentList.size() <= 0){
                        noAppoinmentCV.setVisibility(View.VISIBLE);
                        appoinmentsRV.setVisibility(View.GONE);
                    }else {
                        adapter = new AppointmentsAdapter(context, appoinmentList);
                        appoinmentsRV.setAdapter(adapter);
                        noAppoinmentCV.setVisibility(View.GONE);
                        appoinmentsRV.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }



    @Override
    public void onAttach(Context context_) {
        super.onAttach(context_);
        context = context_ = getActivity();
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
        sv.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sv.setOnQueryTextListener( this);
        sv.setIconifiedByDefault(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                A247.getFirebaseAuth().signOut();
                startActivity(new Intent(context, Registration.class));
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
            Log.d(TAG, "onQueryTextChange: " + e.getMessage());
        }
        return false;
    }
}
