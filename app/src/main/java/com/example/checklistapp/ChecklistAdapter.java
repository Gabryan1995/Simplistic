package com.example.checklistapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

        void bindTo(Boolean currentCheckbox, String currentTask) {
            customChecklistItem.setChecked(currentCheckbox);
            customChecklistItem.setTask(currentTask);
        }
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomChecklistItem itemView = new CustomChecklistItem(parent.getContext());

        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        itemView.setOrientation(LinearLayout.HORIZONTAL);
        itemView.setPadding(20, 0, 20, 0);

        return new ChecklistViewHolder(itemView, new CustomTaskListener());
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        holder.customTaskListener.updatePosition(position);
        holder.bindTo(checkboxes.get(position), tasks.get(position));

        holder.customChecklistItem.mCheckboxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkboxes.set(position, !checkboxes.get(position));
                holder.customChecklistItem.setChecked(checkboxes.get(position));
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
        int position = 0;

        public void updatePosition(int position) {
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
