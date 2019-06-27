package com.doctor.a247.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Nurul Islam on 4/29/19
 */
public class InstanceService extends FirebaseInstanceIdService {

    private static final String TAG = InstanceService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshTokern = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "onTokenRefresh: " + refreshTokern);

    }


}
