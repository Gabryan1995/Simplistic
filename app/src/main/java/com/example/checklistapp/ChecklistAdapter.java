package com.example.checklistapp;

import android.app.Application;
import android.content.Context;
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

    public class ChecklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomChecklistItem customChecklistItem;
        public CustomTaskListener customTaskListener;

        public ChecklistViewHolder(View view, CustomTaskListener customTaskListener) {
            super(view);
            customChecklistItem = (CustomChecklistItem) view;

            customChecklistItem.mCheckboxImage.setOnClickListener(this);
            this.customTaskListener = customTaskListener;
            customChecklistItem.task.addTextChangedListener(customTaskListener);
            customChecklistItem.mDeleteButtonImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.equals(customChecklistItem.mCheckboxImage)) {
                customChecklistItem.isChecked = !customChecklistItem.isChecked;
                if (customChecklistItem.isChecked) {
                    customChecklistItem.mCheckboxImage.setImageDrawable(customChecklistItem.getResources().getDrawable(R.drawable.ic_checkbox_selected));
                    customChecklistItem.task.setEnabled(false);
                } else {
                    customChecklistItem.mCheckboxImage.setImageDrawable(customChecklistItem.getResources().getDrawable(R.drawable.ic_checkbox_deselected));
                    customChecklistItem.task.setEnabled(true);
                }
                checkboxes.set(getAdapterPosition(), customChecklistItem.isChecked);
            } else if (view.equals(customChecklistItem.mDeleteButtonImage)) {
                checkboxes.remove(getAdapterPosition());
                tasks.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            }
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
        holder.customChecklistItem.setTask(checkboxes.get(position), tasks.get(position));
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
