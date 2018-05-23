package com.example.alexchar.studyorganizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class AlertReceiver extends BroadcastReceiver {
    private static AlertReceiver instance = null;
    private String title = "Έχετε ενεργό task", message =  "Πατήστε για περισσότερα";

//    protected AlertReceiver() {
//        // Exists only to defeat instantiation.
//    }
//
//    public static AlertReceiver getInstance() {
//        if(instance == null) {
//            instance = new AlertReceiver();
//        }
//        return instance;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title,message);
        notificationHelper.getManager().notify(1, nb.build());
    }

}
