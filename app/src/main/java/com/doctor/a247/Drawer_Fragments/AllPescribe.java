package com.doctor.a247.Drawer_Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.Pescribe;
import com.doctor.a247.R;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.activity.Registration;
import com.doctor.a247.adapter.PescribeAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPescribe extends Fragment {

    private static final String TAG = AllPescribe.class.getSimpleName();
    private Context context;

    private RecyclerView allPescribeRV;
    private ArrayList<Pescribe> pescribes;
    private PescribeAdapter adapter;

    public AllPescribe() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_pescribe, container, false);
        allPescribeRV = v.findViewById(R.id.allAllPescribeRV);

        try {
            A247.prescription()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null){
                                pescribes = new ArrayList<Pescribe>();
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    Pescribe pescribe = d.getValue(Pescribe.class);
                                    if (TextUtils.equals(pescribe.getPatientId(), A247.getUserId())){
                                        pescribes.add(d.getValue(Pescribe.class));
                                    }

                                }
                                if (pescribes.size() > 0){
                                    adapter = new PescribeAdapter(context, pescribes);
                                    allPescribeRV.setAdapter(adapter);
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

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.logout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout){
            startActivity(new Intent(context, Registration.class));
            A247.getFirebaseAuth().signOut();
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context =getActivity();
    }
}
