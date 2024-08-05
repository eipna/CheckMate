package com.serbi.checkmate.data.interfaces;

public interface TaskListener {
    void onTaskClick(int position);             // On task item click
    void onTaskCompleteClick(int position);     // On task complete button click
}