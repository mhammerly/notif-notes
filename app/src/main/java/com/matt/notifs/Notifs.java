package com.matt.notifs;

import android.app.Application;
import android.content.Context;

/*
 * Created by Matt Hammerly on July 28, 2017
 */
public class Notifs extends Application {

    private static Notifs instance;

    public static Notifs getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
