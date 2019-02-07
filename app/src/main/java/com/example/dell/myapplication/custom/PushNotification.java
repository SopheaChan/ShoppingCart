package com.example.dell.myapplication.custom;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.example.dell.myapplication.MainActivity;
import com.example.dell.myapplication.R;

public class PushNotification {
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID= "4565";
    private static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";
    private static final int importanceLevel = NotificationManager.IMPORTANCE_DEFAULT;
    private NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importanceLevel);
    private NotificationManager notificationManager;
    private Notification notification;
    private String contentText;

    public PushNotification(Context context, String companyName, String productName){

        contentText = "Congratulations, you have successfully purchased "
                +productName+" from our company."+"\n"+"Thank you.";
        this.notificationChannel.enableLights(true);
        this.notificationChannel.setLightColor(Color.rgb(66, 101, 244));

        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        notification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(companyName)
                .setContentText(contentText)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setShowWhen(true)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setStyle(new Notification.BigTextStyle().bigText(contentText))
                .setSmallIcon(R.drawable.ic_add_shopping_cart_blue_24dp).build();
        notificationChannel.enableVibration(true);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
