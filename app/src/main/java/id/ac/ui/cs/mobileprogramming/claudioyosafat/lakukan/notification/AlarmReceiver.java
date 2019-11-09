package id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.claudioyosafat.lakukan.ui.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private final String NOTIFICATION_CHANNEL_ID = "todoId";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "TODO_CHANNEL", importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        int notificationId = intent.getIntExtra("notificationId", 0);
        String todoTitle = intent.getStringExtra("todoTitle");

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, mainIntent,
                0);

        Notification.Builder builder = new Notification.Builder(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(todoTitle).setContentText("Yuk Lakukan")
                    .setChannelId(NOTIFICATION_CHANNEL_ID)
                    .setWhen(System.currentTimeMillis()).setAutoCancel(true)
            .setContentIntent(contentIntent);
        }

        notificationManager.notify(notificationId, builder.build());
    }
}
