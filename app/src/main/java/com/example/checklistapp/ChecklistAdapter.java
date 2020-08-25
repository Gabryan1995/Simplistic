package com.example.checklistapp;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder> {

    private Context context;
    private ArrayList<Boolean> checkboxes;
    private ArrayList<String> tasks;

    ChecklistAdapter(Context context, Checklist checklist) {
        this.context = context;
        checkboxes = checklist.getCheckboxes();
        tasks = checklist.getTasks();
    }

    @NonNull
    @Override
    public ChecklistAdapter.ChecklistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CustomChecklistItem itemView = new CustomChecklistItem(parent.getContext());
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        itemView.task.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                itemView.task.setCursorVisible(false);
                if (event != null && (actionId == EditorInfo.IME_ACTION_DONE)){
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemView.task.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        return new ChecklistViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistAdapter.ChecklistViewHolder holder, final int position) {
        holder.customChecklistItem.setChecked(checkboxes.get(position));
        holder.customChecklistItem.setTask(tasks.get(position));
        holder.customChecklistItem.task.requestFocus();
    }

    @Override
    public int getItemCount() { return checkboxes.size(); }

    public class ChecklistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CustomChecklistItem customChecklistItem;
        final ChecklistAdapter mAdapter;

        public ChecklistViewHolder(View view, ChecklistAdapter adapter) {
            super(view);
            customChecklistItem = (CustomChecklistItem) view;
            mAdapter = adapter;

            customChecklistItem.mDeleteButtonImage.setOnClickListener(this);
        }

        public CustomChecklistItem getCustomChecklistItem() {
            return customChecklistItem;
        }

        @Override
        public void onClick(View view) {
            if (view.equals(customChecklistItem.mDeleteButtonImage)) {
                mAdapter.checkboxes.remove(getAdapterPosition());
                mAdapter.tasks.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            }
        }
    }
}
