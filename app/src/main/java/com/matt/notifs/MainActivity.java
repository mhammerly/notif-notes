package com.matt.notifs.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
                try {
                    int notifId = Integer.parseInt(
                            pref.getKey().substring(Constants.PREFS_NOTIF_PREFIX.length()));
                    notifMgr.notify(
                        notifId,
                        NotifFactory.CreateReminderNotif(
                            getApplicationContext(),
                            notifId,
                            (CharSequence) pref.getValue()));
                } catch (Exception e) { }
            }
        }
    }
}
