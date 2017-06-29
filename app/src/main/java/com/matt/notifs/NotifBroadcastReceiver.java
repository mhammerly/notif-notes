package com.matt.notifs;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import com.matt.notifs.R;
import com.matt.notifs.NotifFactory;

public class NotifBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.android.messagingservice.ACTION_MESSAGE_REPLY")) {
            CharSequence message = getReplyMessage(intent);
            int messageId = intent.getIntExtra("message_id", 0);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, NotifFactory.CreateInputNotif(context));
            notificationManager.notify(2, NotifFactory.CreateReminderNotif(context, message));
        }
    }

    private CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence("reply");
        }
        return null;
    }
}
