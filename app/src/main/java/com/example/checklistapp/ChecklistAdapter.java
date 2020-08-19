package com.example.checklistapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private Context context;

    ChecklistAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChecklistAdapter.ChecklistViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder {
        private CustomChecklistItem customChecklistItem;

        public ChecklistViewHolder(View view) {
            super(view);
            customChecklistItem = (CustomChecklistItem) view;
        }

        public CustomChecklistItem getCustomChecklistItem() {
            return customChecklistItem;
        }
    }
}
