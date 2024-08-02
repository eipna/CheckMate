package com.serbi.checkmate.data.model;

public class TaskModel {

    private final int id;
    private final String name;
    private final String notes;
    private final int priority;
    private final long dateCreated;
    private final long lastEdited;
    private final int isCompleted;

    public TaskModel(int id, String name, String notes, int priority, long dateCreated, long lastEdited, int isCompleted) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.priority = priority;
        this.dateCreated = dateCreated;
        this.lastEdited = lastEdited;
        this.isCompleted = isCompleted;
    }

    public int getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public long getLastEdited() {
        return lastEdited;
    }
}