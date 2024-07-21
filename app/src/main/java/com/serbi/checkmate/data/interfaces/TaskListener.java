package com.serbi.checkmate.data.interfaces;

public interface TaskListener {
    void onTaskClick(int position);
    void onTaskCheck(int position, boolean status);
}