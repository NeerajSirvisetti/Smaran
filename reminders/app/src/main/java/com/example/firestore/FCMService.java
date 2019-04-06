package com.example.firestore;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived: "+remoteMessage);
        if(remoteMessage.getData()!=null) {
            Log.i(TAG, "onMessageReceived: "+remoteMessage.getData().toString());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
