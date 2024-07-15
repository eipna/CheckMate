package com.serbi.checkmate.data.model;

public class TaskModel {

    private int id;
    private String name;
    private String notes;
    private int isCompleted;

    public TaskModel(int id, String name, String notes, int isCompleted) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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