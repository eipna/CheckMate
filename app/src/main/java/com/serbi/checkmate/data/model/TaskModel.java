package com.serbi.checkmate.data.model;

public class TaskModel {

    private int id;
    private String name;
    private String notes;
    private long dateCreated;
    private int isCompleted;

    public TaskModel(int id, String name, String notes, long dateCreated, int isCompleted) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.dateCreated = dateCreated;
        this.isCompleted = isCompleted;
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
}