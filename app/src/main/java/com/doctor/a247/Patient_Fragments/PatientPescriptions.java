package com.doctor.a247.Patient_Fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.doctor.a247.A247;
import com.doctor.a247.Pojos.Pescribe;
import com.doctor.a247.R;
import com.doctor.a247.activity.PatientDetails;
import com.doctor.a247.adapter.PescribeAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientPescriptions extends Fragment {

    private static final String TAG = PatientPescriptions.class.getSimpleName();
    private Context context;

    private CardView addPescribeCV;
    private RecyclerView allPescribeRV;
    private ArrayList<Pescribe> pescribes;
    private PescribeAdapter adapter;
    private PescribeUploadAsync async;

    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    private StorageReference storageReference;
    private final int SELECT_IMAGE = 12;
    private Uri pic_uri;

    public PatientPescriptions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_pescriptions, container, false);
        addPescribeCV = v.findViewById(R.id.pescribeAddCV);
        allPescribeRV = v.findViewById(R.id.allAllPescribeRV);

        addPescribeCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()){
                    Intent pic = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pic.setType("image/*");
                    startActivityForResult(pic.createChooser(pic, "Select Image"), SELECT_IMAGE);
                }
            }
        });

        try {
            A247.prescription()
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null){
                                pescribes = new ArrayList<Pescribe>();
                                for (DataSnapshot d: dataSnapshot.getChildren()){
                                    Pescribe pescribe = d.getValue(Pescribe.class);
                                    if (TextUtils.equals(pescribe.getPatientId(), PatientDetails.getAppoint().getPatientid())){
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context = getActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE){
            try {
                pic_uri = data.getData();
                async = new PescribeUploadAsync();
                async.execute();
            }catch (Exception e){
                Toast.makeText(getActivity(), "Image Not set", Toast.LENGTH_SHORT).show();
            }

        }

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
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime =  MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void pescriptionUpload() {

        CharSequence s = DateFormat.format("MM-dd-yy-hh-mm", new Date().getTime());
        final String pushKey = A247.getDatabaseReference().push().getKey().toString();
        storageReference = A247.patientPescribeImage(PatientDetails.getAppoint().getPatientid(), A247.getUserId()).child(s + pic_uri.getLastPathSegment() + "." + getFileExtension(pic_uri));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pic_uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] dat = baos.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(dat);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Pescribe pescribe = new Pescribe();
                        pescribe.setPescribeId(pushKey);
                        pescribe.setImageLink(task.getResult().toString());
                        pescribe.setAppointmentId(PatientDetails.getAppoint().getId());
                        pescribe.setPatientId(PatientDetails.getAppoint().getPatientid());
                        A247.prescription().child(pushKey).setValue(pescribe);
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){}

    }

    private class PescribeUploadAsync extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            pescriptionUpload();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
