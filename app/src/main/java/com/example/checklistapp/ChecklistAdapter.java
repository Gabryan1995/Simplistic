package com.example.checklistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private ArrayList<Boolean> checkboxes;
    private ArrayList<String> tasks;

    ChecklistAdapter(Checklist checklist) {
        checkboxes = checklist.getCheckboxes();
        tasks = checklist.getTasks();
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomChecklistItem itemView = new CustomChecklistItem(parent.getContext());
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ChecklistViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        holder.customChecklistItem.setChecked(checkboxes.get(position));
        holder.customChecklistItem.setTask(tasks.get(position));
    }

    @Override
    public int getItemCount() { return checkboxes.size(); }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private CustomChecklistItem customChecklistItem;
        final ChecklistAdapter mAdapter;

        public ChecklistViewHolder(View view, ChecklistAdapter adapter) {
            super(view);
            customChecklistItem = (CustomChecklistItem) view;
            mAdapter = adapter;
        }

        public CustomChecklistItem getCustomChecklistItem() {
            return customChecklistItem;
        }
    }
}
