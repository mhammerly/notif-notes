package com.matt.notifs;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForegroundViewManager implements MemoManager.MemoListener {
    private Context mContext;
    private MemoManager mMemoMgr;
    private RecyclerView mRecyclerView;
    private TextView mNotifInput;
    private RecyclerView.LayoutManager mLayoutManager;
    private MemoAdapter mAdapter;
    private Button mSaveButton;

    public ForegroundViewManager(Context context, MemoManager memoMgr) {
        mContext = context;
        mMemoMgr = memoMgr;
        mMemoMgr.registerListener(this);

        List<MemoManager.Memo> memos = mMemoMgr.getMemos();
        Log.i(Constants.LOG_TAG, "Creating foreground view with " + memos.size() + " cached memos");

        Activity activity = (Activity) mContext;

        mRecyclerView = activity.findViewById(R.id.notif_recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MemoAdapter(memos, mContext);
        mRecyclerView.setAdapter(mAdapter);

        mNotifInput = activity.findViewById(R.id.notif_input);
        mSaveButton = activity.findViewById(R.id.notif_save);
        mSaveButton.setOnClickListener(v -> {
            CharSequence text = mNotifInput.getText();
            if (!text.equals("")) {
                mMemoMgr.createMemo(mNotifInput.getText().toString());
                mNotifInput.setText("");
            }
        });
    }

    @Override
    public void memoCreated(MemoManager.Memo memo) {
        Log.i(Constants.LOG_TAG, "FgViewMgr: adding memo " + memo.toString());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void memoDeleted(int id) {
        Log.i(Constants.LOG_TAG, "FgViewMgr: removing memo " + id);
        mRecyclerView.setAdapter(mAdapter);
    }

    // Hack to force redraw when resuming activity
    // Call after memoMgr.refresh()
    public void refresh() {
        Log.i(Constants.LOG_TAG, "Refreshing ForegroundViewManager");
        mAdapter.setData(mMemoMgr.getMemos());
        mRecyclerView.setAdapter(mAdapter);
    }
}