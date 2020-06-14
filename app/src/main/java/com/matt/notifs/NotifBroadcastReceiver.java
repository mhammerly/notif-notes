package com.matt.notifs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.RemoteInput;

public class NotifBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Constants.REPLY_ACTION)) {
            CharSequence message = getReplyMessage(intent);
            Log.i(Constants.LOG_TAG, "Received REPLY_ACTION intent");
            if (!message.equals("")) {
                MemoManager.Memo memo = MemoManager.Static.createMemo(context, message);
                NotifManager.Static.displayMemoNotif(context, memo);
            } else {
                Log.i(Constants.LOG_TAG, "Ignoring empty memo");
            }

            // Reset the input notification to accept more text
            NotifManager.Static.displayInputNotif(context);
        } else if (intent.getAction().equals(Constants.DISMISS_ACTION)) {
            int notifId = intent.getExtras().getInt(Constants.KEY_NOTIF_ID);
            Log.i(Constants.LOG_TAG, "Received DISMISS_ACTION intent for memo " + notifId);
            MemoManager.Static.deleteMemo(context, notifId);
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
