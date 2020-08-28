package com.example.checklistapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    public static ArrayList<Boolean> checkboxes;
    public static ArrayList<String> tasks;

    ChecklistAdapter(Checklist checklist) {
        checkboxes = checklist.getCheckboxes();
        tasks = checklist.getTasks();
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {
        public CustomChecklistItem customChecklistItem;
        public CustomTaskListener customTaskListener;

        public ChecklistViewHolder(View view, CustomTaskListener customTaskListener) {
            super(view);
            customChecklistItem = (CustomChecklistItem) view;

            this.customTaskListener = customTaskListener;
            customChecklistItem.task.addTextChangedListener(customTaskListener);
        }
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomChecklistItem itemView = new CustomChecklistItem(parent.getContext());
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ChecklistViewHolder(itemView, new CustomTaskListener());
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        holder.customTaskListener.setPosition(position);
        holder.customChecklistItem.setChecked(checkboxes.get(position));
        holder.customChecklistItem.setTask(tasks.get(position));

        holder.customChecklistItem.mCheckboxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.customChecklistItem.setChecked(!holder.customChecklistItem.isChecked);
                checkboxes.set(position, holder.customChecklistItem.isChecked);
            }
        });

        holder.customChecklistItem.task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.customChecklistItem.task.requestFocus();
                holder.customChecklistItem.task.setCursorVisible(true);
            }
        });

        holder.customChecklistItem.mDeleteButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkboxes.remove(position);
                tasks.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, checkboxes.size());
            }
        });
    }

    @Override
    public int getItemCount() { return checkboxes.size(); }

    private class CustomTaskListener implements TextWatcher {
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tasks.set(position, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
