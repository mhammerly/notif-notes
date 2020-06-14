package com.matt.notifs;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoViewHolder> {
    private List<MemoManager.Memo> mData;

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

    public MemoAdapter(List<MemoManager.Memo> memos) {
        setData(memos);
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new MemoViewHolder(view);
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

    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_view_item;
    }

    public void setData(List<MemoManager.Memo> memos) {
        mData = memos;
        notifyDataSetChanged();
    }
}
