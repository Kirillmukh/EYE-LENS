package com.example.eyelens;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ActionNotify extends BroadcastReceiver {
    private static final int NOTIFY_ID = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, FinalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "EYELENS")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("EYELENS")
                .setContentText("Срок истек. Замените линзы!")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFY_ID, builder.build());
    }
}
