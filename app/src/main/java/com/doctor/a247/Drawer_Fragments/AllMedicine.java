package com.doctor.a247.Drawer_Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.Medicine;
import com.doctor.a247.R;
import com.doctor.a247.activity.MainActivity;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.activity.Registration;
import com.doctor.a247.adapter.MedicineAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMedicine extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = AllMedicine.class.getSimpleName();
    private Context context;

    private RecyclerView allPatientRV;

    private ArrayList<Medicine> medicines;
    private MedicineAdapter adapter;

    public AllMedicine() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_medicine, container, false);

        allPatientRV = v.findViewById(R.id.allPatientRV);

        try {
            A247.medicine().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        medicines = new ArrayList<Medicine>();
                        for (DataSnapshot d: dataSnapshot.getChildren()){
                            Medicine medicine = d.getValue(Medicine.class);
                            if (TextUtils.equals(medicine.getPatientId(), A247.getUserId())){
                                medicines.add(d.getValue(Medicine.class));
                            }
                        }
                        if (medicines.size() <= 0){
                            allPatientRV.setVisibility(View.GONE);
                        }else {
                            allPatientRV.setVisibility(View.VISIBLE);
                            adapter = new MedicineAdapter(context, medicines);
                            allPatientRV.setAdapter(adapter);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.d(TAG, "onCreateView: " + e.getMessage());
        }

        return  v;
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
            Log.d(TAG, "onQueryTextChange: " + e.getMessage());
        }
        return false;
    }
}
