package com.example.checklistapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> implements Filterable {

    ArrayList<Checklist> checklists;
    ArrayList<Checklist> checklistsFiltered;
    Context context;

    public MainAdapter(Context context, ArrayList<Checklist> checklists) {
        this.context = context;
        this.checklists = checklists;
        this.checklistsFiltered = checklists;
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
        holder.title.setText(checklistsFiltered.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChecklistActivity.class);
                intent.putExtra(MainActivity.PARCELABLE_KEY, checklists.get(position));
                intent.putExtra(MainActivity.POSITION_KEY, position);
                ((MainActivity) context).startActivityForResult(intent, MainActivity.CHECKLIST_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return checklistsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    checklistsFiltered = checklists;
                } else {
                    ArrayList<Checklist> filteredList = new ArrayList<>();
                    for (Checklist row : checklists) {
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    checklistsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = checklistsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                checklistsFiltered = (ArrayList<Checklist>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
