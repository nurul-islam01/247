package com.doctor.a247.Manager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

import com.doctor.a247.R;
import com.doctor.a247.activity.MainActivity;

/**
 * Created by Nurul Islam on 4/29/19
 */
public class FirebaseNotification {

    private Context context;



    private static final String CHANNEL_ID = "a247id";
    private static final String CHANNEL_NAME = "a247name";
    private static final String CHANNEL_DESC = "a247desc";

    public FirebaseNotification(Context context) {
        this.context = context;

    }

    public void showNotification(String title, String body){

        Intent intent = new Intent( context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 111, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.doctor_icon_small_white);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.doctor_icon_big));
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(11, builder.build());

    }

}
