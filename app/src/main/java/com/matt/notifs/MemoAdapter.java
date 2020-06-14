package com.matt.notifs;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private MemoManager mMemoMgr;

    public static class MemoViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        public TextView mText;
        public TextView mId;

        public MemoViewHolder(View view) {
            super(view);
            mView = view;
            mText = view.findViewById(R.id.memo_text);
            mId = view.findViewById(R.id.memo_id);
        }
    }

    public MemoAdapter(MemoManager memoMgr) {
        mMemoMgr = memoMgr;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MemoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        MemoManager.Memo notif = mMemoMgr.getMemos().get(position);
        holder.mText.setText(notif.getText());
        holder.mId.setText(Integer.toString(notif.getId()));
    }

    @Override
    public int getItemCount() {
        return mMemoMgr.getMemos().size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_view_item;
    }

    // position is not memo id!
    public void deleteItem(int position) {
        int id = mMemoMgr.getMemos().get(position).getId();
        Log.i(Constants.LOG_TAG, "Deleting memo " + id + " from recycler view");
        mMemoMgr.deleteMemo(id);
    }
}
