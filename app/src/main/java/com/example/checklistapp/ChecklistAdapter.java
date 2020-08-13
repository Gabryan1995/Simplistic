package com.example.checklistapp;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ViewHolder> {

    @NonNull
    @Override
    public ChecklistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CustomChecklistItem customChecklistItem;

        public ViewHolder(View view) {
            super(view);
            customChecklistItem = (CustomChecklistItem) view;
        }

        public CustomChecklistItem getCustomChecklistItem() {
            return customChecklistItem;
        }
    }
}
