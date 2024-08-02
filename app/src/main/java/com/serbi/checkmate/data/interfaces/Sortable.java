package com.serbi.checkmate.data.interfaces;

public interface Sortable {
    void sortNameAZ();          // Sorting names by ascending
    void sortNameZA();          // Sorting names by descending
    void sortDateCreatedAcs();  // Sorting date created timestamp by Ascending
    void sortDateCreatedDesc(); // Sorting date created timestamp by Descending
    void sortLastEditedAsc();   // Sorting last edited timestamp by Ascending
    void sortLastEditedDesc();  // Sorting last edited timestamp by Descending
}