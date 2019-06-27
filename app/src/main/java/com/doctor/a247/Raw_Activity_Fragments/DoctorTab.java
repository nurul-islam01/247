package com.doctor.a247.Raw_Activity_Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.a247.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorTab extends Fragment {


    public DoctorTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_tab, container, false);
    }

}
