package com.matt.notifs;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import com.matt.notifs.Constants;
import com.matt.notifs.R;
import com.matt.notifs.NotifBroadcastReceiver;

public class NotifFactory {

    public static Notification CreateInputNotif(Context context) {
        RemoteInput remoteInput = new RemoteInput.Builder(Constants.KEY_REPLY)
            .setLabel("Enter some shit to remember")
            .build();

        Intent intent = new Intent(context, NotifBroadcastReceiver.class);
        intent.setAction(Constants.REPLY_ACTION);
        intent.putExtra(Constants.KEY_NOTIF_ID, Constants.INPUT_NOTIF_ID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Constants.INPUT_NOTIF_ID, intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_secure, "Enter some shit to remember", pendingIntent)
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build();

        return new NotificationCompat.Builder(context)
            .setContentText("expand to create reminders")
            .setSmallIcon(android.R.color.transparent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setStyle(new NotificationCompat.BigTextStyle())
            .addAction(replyAction)
            .build();
    }

    public static Notification CreateReminderNotif(Context context, int notifId, CharSequence message) {
        Intent intent = new Intent(context, NotifBroadcastReceiver.class);
        intent.setAction(Constants.DISMISS_ACTION);
        intent.putExtra(Constants.KEY_NOTIF_ID, notifId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifId, intent,
                                            PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(context)
            .setContentTitle("Reminder")
            .setContentText(message)
            .setSmallIcon(R.drawable.noun_notification_619387)
            .setDeleteIntent(pendingIntent)
            .build();
    }
}
