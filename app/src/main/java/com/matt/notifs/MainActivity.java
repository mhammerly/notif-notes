package com.matt.notifs.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.matt.notifs.Constants;
import com.matt.notifs.R;
import com.matt.notifs.NotifFactory;

import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager notifMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifMgr.notify(1, NotifFactory.CreateInputNotif(getApplicationContext()));

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, 0);
        Map<String, ?> allPrefs = prefs.getAll();

        for (Map.Entry<String, ?> pref : allPrefs.entrySet()) {
            if (pref.getKey().startsWith(Constants.PREFS_NOTIF_PREFIX)) {
                String notifKey = pref.getKey().substring(Constants.PREFS_NOTIF_PREFIX.length());
                Log.i(Constants.LOG_TAG,
                    "Found saved notif on startup " +
                    "{key: " + notifKey +
                    ", value: " + pref.getValue() +
                    "}");
                try {
                    int notifId = Integer.parseInt(notifKey);
                    notifMgr.notify(
                        notifId,
                        NotifFactory.CreateReminderNotif(
                            getApplicationContext(),
                            notifId,
                            (CharSequence) pref.getValue()));
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG,
                        "Failed to create notif " +
                        "{key: " + notifKey +
                        ", value: '" + pref.getValue() +
                        "'}: " + e.getMessage());
                }
            }
        }
    }
}
