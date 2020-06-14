package com.matt.notifs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MemoManager implements MemoListener {
    private Context mContext;
    private SharedPreferences mPrefs;
    private PrefsListener mPrefsListener = new PrefsListener();

    @Nullable
    private ArrayList<Memo> mMemoizedMemos = null;

    public static class Static {
        public static Memo createMemo(Context context, CharSequence text) {
            SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = prefs.edit();

            int latestNotif = prefs.getInt(Constants.PREFS_LATEST_NOTIF, 1);
            int notifId = latestNotif + 1;
            String notifKey = Integer.toString(notifId);
            editor.putString(Constants.PREFS_NOTIF_PREFIX + notifKey, text.toString());
            editor.putInt(Constants.PREFS_LATEST_NOTIF, notifId);
            editor.commit();

            Memo memo = new Memo(text, notifId);
            Log.i(Constants.LOG_TAG, "Created memo " + memo.toString());
            return memo;
        }

        public static void deleteMemo(Context context, int id) {
            SharedPreferences prefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = prefs.edit();

            String notifKey = Integer.toString(id);
            editor.remove(Constants.PREFS_NOTIF_PREFIX + notifKey);
            editor.commit();
            Log.i(Constants.LOG_TAG, "Deleted memo " + id);
        }
    }

    private static class PrefsListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        private ArrayList<MemoListener> mListeners = new ArrayList<>();

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.startsWith(Constants.PREFS_NOTIF_PREFIX)) {
                String idStr = key.substring(Constants.PREFS_NOTIF_PREFIX.length());
                try {
                    int id = Integer.parseInt(idStr);
                    if (prefs.contains(key)) {
                        String text = prefs.getString(key, "");
                        Memo memo = new Memo(text, id);
                        for (MemoListener listener : mListeners) {
                            listener.memoCreated(memo);
                        }
                    } else {
                        for (MemoListener listener : mListeners) {
                            listener.memoDeleted(id);
                        }
                    }
                } catch (Exception e) {}
            }
        }

        public void registerListener(MemoListener listener) {
            mListeners.add(listener);
        }

    };

    public static class Memo {
        private CharSequence mText;
        private int mId;

        public Memo(CharSequence text, int id) {
            mText = text;
            mId = id;
        }

        public int getId() {
            return mId;
        }

        public CharSequence getText() {
            return mText;
        }

        public String toString() {
            return mId + ": " + mText.toString();
        }
    }

    @Override
    public void memoCreated(Memo memo) {
        mMemoizedMemos.add(memo);
    }

    @Override
    public void memoDeleted(int id) {
        mMemoizedMemos.removeIf(memo -> (memo.getId() == id));
    }


    public MemoManager(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        mPrefs.registerOnSharedPreferenceChangeListener(mPrefsListener);

        mPrefsListener.registerListener(this);
    }

    public void registerListener(MemoListener listener) {
        mPrefsListener.registerListener(listener);
    }

    public void createMemo(CharSequence text) {
        Static.createMemo(mContext, text);
    }

    public void deleteMemo(int id) {
        Static.deleteMemo(mContext, id);
    }

    public List<Memo> getMemos() {
        if (mMemoizedMemos == null) {
            Log.i(Constants.LOG_TAG, "Reading memos from SharedPrefs...");
            mMemoizedMemos = new ArrayList<>();
            Map<String, ?> allPrefs = mPrefs.getAll();

            for (Map.Entry<String, ?> pref : allPrefs.entrySet()) {
                if (pref.getKey().startsWith(Constants.PREFS_NOTIF_PREFIX)) {
                    String notifKey = pref.getKey().substring(Constants.PREFS_NOTIF_PREFIX.length());
                    try {
                        int notifId = Integer.parseInt(notifKey);
                        mMemoizedMemos.add(new Memo((CharSequence) pref.getValue(), notifId));
                    } catch (Exception e) {
                    }
                }
            }
            mMemoizedMemos.sort((l, r) -> Integer.compare(l.getId(), r.getId()));
            Log.i(Constants.LOG_TAG, "Found " + mMemoizedMemos.size() + " memos in SharedPrefs");
        }
        return mMemoizedMemos;
    }
}
