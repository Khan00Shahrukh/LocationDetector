package com.example.androidrealtimelocation2021.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.androidrealtimelocation2021.R;

public class NotificationHelper  extends ContextWrapper {
    private static final String SRK_CHANNEL_ID="com.example.androidrealtimelocation2021";
    private static final String SRK_CHANNEL_NAME="Realtime2021";

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createChannel ();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel srkChannel = new NotificationChannel(SRK_CHANNEL_ID,SRK_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        srkChannel.enableLights(false);
        srkChannel.enableVibration(true);
        srkChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(srkChannel);    }

    public NotificationManager getManager() {
        if(manager == null)
            manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getRealtimeTrackingNotification(String title, String content, Uri defaultSound) {
        return  new Notification.Builder(getApplicationContext(),SRK_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(defaultSound)
                .setAutoCancel(false);
    }
}
