package com.matt.notifs;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    private MemoManager mMemoMgr;
    private NotifManager mNotifMgr;
    private ForegroundViewManager mFgViewMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMemoMgr = new MemoManager(this);
        mNotifMgr = new NotifManager(this, mMemoMgr);
        mNotifMgr.displayInputNotif();
        mFgViewMgr = new ForegroundViewManager(this, mMemoMgr);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Hack to redraw recyclerview
        // Must call in this order
        mMemoMgr.refresh();
        mFgViewMgr.refresh();
    }
}
