package com.example.checklistapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    ArrayList<Checklist> checklists;
    Context context;

    public MainAdapter(Context context, ArrayList<Checklist> checklists) {
        this.context = context;
        this.checklists = checklists;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MainViewHolder viewHolder = new MainViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, final int position) {
        holder.title.setText(checklists.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Selected Checklist Titled: " + checklists.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklists.size();
    }


    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        int position;

        public MainViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.checklist_item_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            position = getLayoutPosition();
        }
    }
}
