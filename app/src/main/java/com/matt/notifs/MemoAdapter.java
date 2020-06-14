package com.matt.notifs;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private List<MemoManager.Memo> mData;
    private Context mContext;

    public static class MemoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLayout;
        public TextView mText;
        public TextView mId;

        public MemoViewHolder(LinearLayout layout) {
            super(layout);
            mLayout = layout;
            mText = (TextView) layout.getChildAt(0);
            mId = (TextView) layout.getChildAt(1);
        }
    }

    public MemoAdapter(List<MemoManager.Memo> memos, Context context) {
        setData(memos);
        mContext = context;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView text = new TextView(mContext);

        TextView id = new TextView(mContext);
        id.setVisibility(View.GONE);

        LinearLayout layout = new LinearLayout(mContext);
        layout.addView(text);
        layout.addView(id);
        return new MemoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position) {
        MemoManager.Memo notif = mData.get(position);
        holder.mText.setText(notif.getText());
        holder.mId.setText(Integer.toString(notif.getId()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<MemoManager.Memo> memos) {
        mData = memos;
        notifyDataSetChanged();
    }
}
