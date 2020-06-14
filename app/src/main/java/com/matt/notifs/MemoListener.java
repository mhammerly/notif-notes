package com.matt.notifs;

public interface MemoListener {
    void memoCreated(MemoManager.Memo memo);
    void memoDeleted(int id);
}
