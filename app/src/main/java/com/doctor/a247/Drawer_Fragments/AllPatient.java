package com.doctor.a247.Drawer_Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.doctor.a247.adapter.AllPatientAdapter;
import com.doctor.a247.model.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllPatient extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = AllPatient.class.getSimpleName();
    private Context context;

    private CardView noPatientCV;
    private RecyclerView allPatientRV;

    private ArrayList<Patient> patientList;
    private AllPatientAdapter adapter;

    public AllPatient() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_patient, container, false);
        allPatientRV = v.findViewById(R.id.allPatientRV);
        noPatientCV = v.findViewById(R.id.noPatientCV);

        noPatientCV.setVisibility(View.GONE);

        A247.allPatients().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        patientList = new ArrayList<Patient>();
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            patientList.add(data.getValue(Patient.class));
                        }

                        if (patientList.size() <= 0){
                            noPatientCV.setVisibility(View.VISIBLE);
                            allPatientRV.setVisibility(View.GONE);
                        }else {
                            adapter = new AllPatientAdapter(context, patientList);
                            allPatientRV.setAdapter(adapter);
                            noPatientCV.setVisibility(View.GONE);
                            allPatientRV.setVisibility(View.VISIBLE);
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

        return  v;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }




    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            adapter.getFilter().filter(newText);
        }catch (Exception e){
            Log.d(TAG, "onQueryTextChange: " + e.getMessage());
        }

        return false;
    }
}
