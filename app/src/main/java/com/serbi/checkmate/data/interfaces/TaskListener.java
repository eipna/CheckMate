package com.serbi.checkmate.data.interfaces;

import android.view.View;

public interface TaskListener {
    void onTaskClick(int position); // On task item click
    void onTaskMoreOptionsClick(int position, View view); // On task item more options button click
}