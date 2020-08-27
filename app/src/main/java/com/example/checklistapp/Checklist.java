package com.example.checklistapp;

import android.content.Intent;

import java.util.ArrayList;

public class Checklist {

    private String title;
    private ArrayList<Boolean> checkboxes = new ArrayList<>();
    private ArrayList<String> tasks = new ArrayList<>();
    private int size;

    public Checklist() {
        this.title = "";
        this.checkboxes.add(false);
        this.tasks.add("");
        this.size = 1;
    }

    public Checklist(String title, ArrayList<Boolean> checkboxes, ArrayList<String> tasks) {
        this.title = title;
        this.checkboxes = checkboxes;
        this.tasks = tasks;
        this.size = checkboxes.size();
    }

    String getTitle() { return title; }

    ArrayList<Boolean> getCheckboxes() { return checkboxes; }

    ArrayList<String> getTasks() { return tasks; }

    public void addEmptyTask() {
        checkboxes.add(false);
        tasks.add("");
        size += 1;
    }

    int size() { return size; }
}
