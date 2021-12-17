package com.example.go4lunch.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;

public class NotificationHelper extends ContextWrapper {

    public static final String IDChannel="ID Channel";
    public static final String nameChannel="name Channel";
    private NotificationManager mManager;

    //SOUND ALARM
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

    }

   // @TargetApi(Build.VERSION_CODES.0)

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(IDChannel, nameChannel, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);

    }

    public NotificationManager getManager() {
        if (mManager == null) {

            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String notifMessage) {
        return new NotificationCompat.Builder(getApplicationContext(), IDChannel)
                .setContentTitle(getString(R.string.alarm_title))
                .setContentText(notifMessage)
                .setSmallIcon(R.drawable.launch_black_72dp)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notifMessage))
                .setSound(alarmSound);
    }
}
