package com.example.checklistapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Checklist implements Parcelable {

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


    /***
     * Parcelable implementation below
     */
    protected Checklist(Parcel in) {
        title = in.readString();
        /*boolean[] temp = in.createBooleanArray();
        for (int i = 0; i < temp.length; i++) {
            checkboxes.add(temp[i]);
        }*/
        in.readList(checkboxes, null);
        in.readList(tasks, null);
        size = checkboxes.size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        /*boolean[] temp = new boolean[checkboxes.size()];
        for (int i = 0; i < checkboxes.size(); i++) {
            temp[i] = checkboxes.get(i);
        }*/
        dest.writeList(checkboxes);
        dest.writeList(tasks);
    }

    public static final Creator<Checklist> CREATOR = new Creator<Checklist>() {
        @Override
        public Checklist createFromParcel(Parcel in) {
            return new Checklist(in);
        }

        @Override
        public Checklist[] newArray(int size) {
            return new Checklist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
