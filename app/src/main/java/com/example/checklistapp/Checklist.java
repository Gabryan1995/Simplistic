package com.example.checklistapp;

import android.content.Intent;

import java.util.ArrayList;

public class Checklist {

    private String title;
    private ArrayList<Boolean> checkboxes;
    private ArrayList<String> tasks;

    public Checklist(String title, ArrayList<Boolean> checkboxes, ArrayList<String> tasks) {
        this.title = title;
        this.checkboxes = checkboxes;
        this.tasks = tasks;
    }

    String getTitle() { return title; }

    ArrayList<Boolean> getCheckboxes() { return checkboxes; }

    ArrayList<String> getTasks() { return tasks; }
}
