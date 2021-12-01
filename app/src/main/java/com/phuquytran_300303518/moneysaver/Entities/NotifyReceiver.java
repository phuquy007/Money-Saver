package com.phuquytran_300303518.moneysaver.Entities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.phuquytran_300303518.moneysaver.Activities.MainActivity;
import com.phuquytran_300303518.moneysaver.Activities.NotificationActivity;
import com.phuquytran_300303518.moneysaver.R;

public class NotifyReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "this.is.my.channelId";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent notificationIntent = new Intent(context, MainActivity.class);//on tap this activity will open

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);//getting the pendingIntent

        Notification.Builder builder = new Notification.Builder(context);//building the notification

        Notification notification = builder.setContentTitle("Money Saver Notification")
                .setContentText("You need to enter the today's expenses and incomes")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//below creating notification channel, because of androids latest update, O is Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notification);
    }
}
