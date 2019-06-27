package com.doctor.a247;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.doctor.a247.activity.MainActivity;
import com.doctor.a247.activity.Registration;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class A247 extends Application {

    private static FirebaseAuth auth;
    private static FirebaseUser user;
    private static FirebaseDatabase database;
    private static FirebaseStorage storage;
    private static StorageReference storageReference;
    public static final String TYPE_DOCTOR = "Doctor";
    public static final String TYPE_PATIENT = "Patient";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseStorage.getInstance().getApp();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (user != null){

            Intent intent = new Intent(A247.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else {
            Intent intent = new Intent(A247.this, Registration.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }



    public static FirebaseAuth getFirebaseAuth(){
        return auth;
    }

    public static String getUserId(){
        return getUser().getUid();
    }

    public static FirebaseUser getUser(){
        return auth.getCurrentUser();
    }

    public static StorageReference getStorageReference(){
        return storageReference;
    }


    public static StorageReference doctorProfilePicStore(){
        return getStorageReference().child(getUserId()).child("ProfilePic");
    }
    public static StorageReference patientPescribeImage(String patientId, String doctorId){
        return getStorageReference().child(patientId).child("Appointment").child(doctorId);
    }

    public static DatabaseReference getDatabaseReference(){
        return database.getReference();
    }

    public static DatabaseReference getUserProfileDatabaseReference(){
        return getDatabaseReference().child("ALL_USER").child(getUserId());
    }

    public static DatabaseReference chamber(String doctorId){
        return getDatabaseReference().child("chambers").child(doctorId);
    }

    public static DatabaseReference problems(String patientId){
        return getDatabaseReference().child("problems").child(patientId);
    }

    public static DatabaseReference getProfileById(String profileId){
        return getDatabaseReference().child("ALL_USER").child(profileId);
    }

    public static DatabaseReference deleteUser(){
        return getDatabaseReference().child("ALL_USER").child(getUserId());
    }

    public static DatabaseReference allPatients(){
        return getDatabaseReference().child("Patient");
    }

    public static DatabaseReference allDoctors(){
        return getDatabaseReference().child("Doctor");
    }

    public static DatabaseReference getUserType(){
        return getDatabaseReference().child("Type").child(getUserId()).child("Type");
    }
    public static DatabaseReference removeUserType(){
        return getDatabaseReference().child("Type").child(getUserId());
    }

    public static DatabaseReference userPhoneNumber(String phoneNumber){
        return getDatabaseReference().child("USERS_NUMBER").child(phoneNumber);
    }

    public static DatabaseReference getUserIDdatabaseReference(String userId){
        return getDatabaseReference().child("ALL_USER").child(userId);
    }


    public static DatabaseReference request(){
        return getDatabaseReference().child("request");
    }

    public static DatabaseReference token(){
        return getDatabaseReference().child("token");
    }

    public static DatabaseReference appointed(){
        return getDatabaseReference().child("appointed");
    }
    public static DatabaseReference medicine(){
        return getDatabaseReference().child("medicine");
    }
    public static DatabaseReference prescription(){
        return getDatabaseReference().child("prescription");
    }


}
