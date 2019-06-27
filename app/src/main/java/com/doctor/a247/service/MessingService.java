package com.doctor.a247.service;

import android.util.Log;

import com.doctor.a247.Manager.FirebaseNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessingService extends FirebaseMessagingService {

    private static final String TAG = MessingService.class.getSimpleName();

    public MessingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            FirebaseNotification notification = new FirebaseNotification(getApplicationContext());
            notification.showNotification(title, body);
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: " + s);
    }
}
