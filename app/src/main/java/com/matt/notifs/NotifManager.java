package com.matt.notifs;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import java.util.List;

import static androidx.core.app.NotificationCompat.CATEGORY_REMINDER;
import static androidx.core.app.NotificationCompat.CATEGORY_SYSTEM;

public class NotifManager implements MemoListener {
    private Context mContext;
    private NotificationManagerCompat mNotifMgr;
    private NotificationChannel mNotifChannel;
    private MemoManager mMemoMgr;

    public static class Static {
        public static void displayInputNotif(Context context) {

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

            Notification notif = new NotificationCompat.Builder(context, Constants.NOTIF_CHANNEL)
                    .setCategory(CATEGORY_SYSTEM)
                    .setContentText("expand to create reminders")
                    .setSmallIcon(android.R.color.transparent)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .addAction(replyAction)
                    .build();

            NotificationManagerCompat notifMgr = NotificationManagerCompat.from(context);
            notifMgr.notify(1, notif);
            Log.i(Constants.LOG_TAG, "Displaying input notif");
        }

        public static void displayMemoNotif(Context context, MemoManager.Memo memo) {
            Intent intent = new Intent(context, NotifBroadcastReceiver.class);
            intent.setAction(Constants.DISMISS_ACTION);
            intent.putExtra(Constants.KEY_NOTIF_ID, memo.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, memo.getId(), intent,
                    PendingIntent.FLAG_IMMUTABLE);

            Notification notif = new NotificationCompat.Builder(context, Constants.NOTIF_CHANNEL)
                    .setCategory(CATEGORY_REMINDER)
                    .setContentTitle("Reminder")
                    .setContentText(memo.getText())
                    .setSmallIcon(R.drawable.noun_notification_619387)
                    .setDeleteIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .build();

            NotificationManagerCompat notifMgr = NotificationManagerCompat.from(context);
            notifMgr.notify(memo.getId(), notif);
            Log.i(Constants.LOG_TAG, "Displaying notif for memo " + memo.toString());
        }
    }

    public NotifManager(Context context, MemoManager memoMgr) {
        mContext = context;
        mNotifMgr = NotificationManagerCompat.from(mContext);
        mMemoMgr = memoMgr;
        mMemoMgr.registerListener(this);

        // I think I can let this non-compat manager destruct now
        NotificationManager nonCompatManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifChannel = new NotificationChannel(Constants.NOTIF_CHANNEL, CATEGORY_REMINDER, NotificationManager.IMPORTANCE_DEFAULT);
        nonCompatManager.createNotificationChannel(mNotifChannel);

        List<MemoManager.Memo> memos = mMemoMgr.getMemos();
        Log.i(Constants.LOG_TAG, "Making notifs for " + memos.size() + " cached memos");
        for (MemoManager.Memo memo : memos) {
            displayMemoNotif(memo);
        }
    }

    @Override
    public void memoCreated(MemoManager.Memo memo) {
        Log.i(Constants.LOG_TAG, "NotifMgr: creating memo " + memo.toString());
        displayMemoNotif(memo);
    }

    @Override
    public void memoDeleted(int id) {
        Log.i(Constants.LOG_TAG, "NotifMgr: removing memo " + id);
        mNotifMgr.cancel(id);
    }

    public void displayInputNotif() {
        Static.displayInputNotif(mContext);
    }

    public void displayMemoNotif(MemoManager.Memo memo) {
        Static.displayMemoNotif(mContext, memo);
    }
}