package com.serbi.checkmate.data.interfaces;

public interface TaskListener {
    void onTaskClick(int position); // On task item click
    void onTaskCheck(int position, boolean status); // On task item check box click
}