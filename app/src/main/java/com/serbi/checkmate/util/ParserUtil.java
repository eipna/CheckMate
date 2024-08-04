package com.serbi.checkmate.util;

import com.serbi.checkmate.App;

public class ParserUtil {

    public static String parsePriorityLevel(int priorityLevel) {
        if (priorityLevel == App.TASK_PRIORITY_HIGH) {
            return "High";
        } else if (priorityLevel == App.TASK_PRIORITY_MEDIUM) {
            return "Medium";
        } else if (priorityLevel == App.TASK_PRIORITY_LOW) {
            return "Low";
        } else if (priorityLevel == App.TASK_PRIORITY_NO_PRIORITY) {
            return "No Priority";
        } else {
            return "null";
        }
    }
}