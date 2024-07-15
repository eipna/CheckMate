package com.serbi.checkmate.data.model;

public class TaskModel {

    private String name;
    private String notes;
    private int isCompleted;

    public TaskModel(String name, String notes, int isCompleted) {
        this.name = name;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }
}