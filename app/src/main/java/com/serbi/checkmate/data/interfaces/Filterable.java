package com.serbi.checkmate.data.interfaces;

public interface Filterable {
    void filterPriorityHigh();      // Filtering task by high priority
    void filterPriorityMedium();    // Filtering task by medium priority
    void filterPriorityLow();       // Filtering task by medium priority
    void filterPriorityDefault();   // Filtering task by no priority
}