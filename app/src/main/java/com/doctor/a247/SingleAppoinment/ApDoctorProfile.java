package com.doctor.a247.SingleAppoinment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.AppointmentedProfile;
import com.doctor.a247.R;
import com.doctor.a247.REG_MODEL.DoctorSignupProfile;
import com.doctor.a247.activity.SingleAppointment;
import com.doctor.a247.model.Appoint;
import com.doctor.a247.model.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ApDoctorProfile extends Fragment {

    private static final String TAG = ApDoctorProfile.class.getSimpleName();
    private Context context;

    private CircleImageView userphotoIV;
    private TextView userNameTV, userdeginationTV, specilityTV, phoneTV, doctorchemberTV, serialandTimeTV, dateTV;

    private Appoint appoint;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ap_doctor_profile, container, false);

        dateTV = view.findViewById(R.id.dateTV);
        userphotoIV = view.findViewById(R.id.userphotoIV);
        userNameTV = view.findViewById(R.id.userNameTV);
        userdeginationTV = view.findViewById(R.id.userdeginationTV);
        specilityTV = view.findViewById(R.id.specilityTV);
        phoneTV = view.findViewById(R.id.phoneTV);
        doctorchemberTV = view.findViewById(R.id.doctorchemberTV);
        serialandTimeTV = view.findViewById(R.id.serialandTimeTV);


        try {
            appoint = SingleAppointment.getAppoint();

            dateTV.setText(appoint.getAppointDate());
            serialandTimeTV.setText("Serial : "+appoint.getSerial()+"  |  Time : "+appoint.getAppointTime());

        }catch (Exception e){
            e.printStackTrace();
        }
        A247.getUserIDdatabaseReference(appoint.getDoctorId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    try {
                        Doctor doctorProfile = dataSnapshot.getValue(Doctor.class);

                        Picasso.get().load(doctorProfile.getImageUrl()).placeholder(R.drawable.doctor_image_icon)
                                .error(R.drawable.doctor_image_icon).into(userphotoIV);

                        userNameTV.setText(doctorProfile.getName());
                        userdeginationTV.setText(doctorProfile.getDegination());
                        specilityTV.setText(doctorProfile.getSpecalist());
                        phoneTV.setText(doctorProfile.getPhoneNumber());
                        doctorchemberTV.setText(doctorProfile.getPresentAddress().getHouseNo());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();

    }

}
