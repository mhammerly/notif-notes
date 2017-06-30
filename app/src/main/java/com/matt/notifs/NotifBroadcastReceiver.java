package com.matt.notifs;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import com.matt.notifs.Constants;
import com.matt.notifs.R;
import com.matt.notifs.NotifFactory;

public class NotifBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.REPLY_ACTION)) {
            CharSequence message = getReplyMessage(intent);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // Reset the input notification to accept more text
            notificationManager.notify(Constants.INPUT_NOTIF_ID, NotifFactory.CreateInputNotif(context));

            // Save this notification and display it.
            SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = prefs.edit();
            int latestNotif = prefs.getInt(Constants.PREFS_LATEST_NOTIF, 1);
            notificationManager.notify(latestNotif + 1, NotifFactory.CreateReminderNotif(context, message));
            editor.putString(Constants.PREFS_NOTIF_PREFIX + Integer.toString(latestNotif + 1), message.toString());
            editor.putInt(Constants.PREFS_LATEST_NOTIF, latestNotif + 1);
            editor.commit();
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
