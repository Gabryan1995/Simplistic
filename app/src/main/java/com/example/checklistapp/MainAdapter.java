package com.example.checklistapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> implements Filterable {

    // Components
    ArrayList<Checklist> checklists;
    ArrayList<Checklist> checklistsFiltered;
    Context context;

    public MainAdapter(Context context, ArrayList<Checklist> checklists) {
        this.context = context;
        this.checklists = checklists;
        this.checklistsFiltered = checklists;
    }

    /**
     * MainAdapter's Custom ViewHolder
     */
    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public MainViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.checklist_item_name);
            itemView.setOnClickListener(this);
        }

        void bindTo(Checklist currentChecklist) {
            title.setText(currentChecklist.getTitle());
        }

        /**
         * Register's user selection to send them to the ChecklistActivity.
         */
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ChecklistActivity.class);
            intent.putExtra(MainActivity.PARCELABLE_KEY, checklists.get(getAdapterPosition()));
            intent.putExtra(MainActivity.POSITION_KEY, getAdapterPosition());
            ((MainActivity) context).startActivityForResult(intent, MainActivity.CHECKLIST_REQUEST_CODE);
        }
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
        holder.bindTo(checklistsFiltered.get(position));
    }

    @Override
    public int getItemCount() {
        return checklistsFiltered.size();
    }

    /**
     * Used to filter the adapter's list of values based on user's query.
     */
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
                    for (Checklist checklist : checklists) {
                        if (checklist.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(checklist);
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
}
