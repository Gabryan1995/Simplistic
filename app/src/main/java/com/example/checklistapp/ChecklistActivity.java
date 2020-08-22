package com.example.checklistapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.checklistapp.MainActivity.INTENT_TITLE_KEY;
import static com.example.checklistapp.MainActivity.INTENT_CHECKBOXES_STATUS_KEY;
import static com.example.checklistapp.MainActivity.INTENT_TASK_KEY;

public class ChecklistActivity extends AppCompatActivity {

    // TODO: HANDLE CHECKLIST ACTIVITY
    // - Send Checklist back to MainActivity
    // - Handle setting up a new checklist, or taking data to edit an existing checklist.

    private Checklist checklist;

    private EditText title;
    private ArrayList<CustomChecklistItem> checklistItems;

    RecyclerView recyclerView;
    ChecklistAdapter checklistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        title = findViewById(R.id.checklist_title);

        /*retrieveChecklist();

        if (checklist != null) {
            for (int i = 0; i < checklist.getCheckboxes().size(); i++) {

            }
        } else {

        }*/

        checklist = new Checklist();
        title.setText(checklist.getTitle(), TextView.BufferType.EDITABLE);

        recyclerView = findViewById(R.id.checklist_item_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacingItemDecoration(25));

        checklistAdapter = new ChecklistAdapter(checklist);
        recyclerView.setAdapter(checklistAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        returnChecklist();
    }

    private void returnChecklist() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_TITLE_KEY, checklist.getTitle());
        returnIntent.putExtra(INTENT_CHECKBOXES_STATUS_KEY, checklist.getCheckboxes());
        returnIntent.putExtra(INTENT_TASK_KEY, checklist.getTasks());
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void retrieveChecklist() {
        Intent data = getIntent();
        String title = data.getStringExtra(INTENT_TITLE_KEY);

        boolean[] checkboxesArray = data.getBooleanArrayExtra(INTENT_CHECKBOXES_STATUS_KEY);
        ArrayList<Boolean> checkboxes = new ArrayList<>();
        for (int i = 0; i < checkboxesArray.length; i++) {
            checkboxes.add(checkboxesArray[i]);
        }

        ArrayList<String> task = data.getStringArrayListExtra(INTENT_TASK_KEY);

        checklist = new Checklist(title, checkboxes, task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_checklist, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addtask:
                checklist.addEmptyTask();
                checklistAdapter.notifyItemInserted(checklist.size());
                recyclerView.smoothScrollToPosition(checklist.size());
        }
        return false;
    }

    public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spacing;

        SpacingItemDecoration(int space) {
            this.spacing = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = spacing;

            if (parent.getChildLayoutPosition(view) == 0)
                outRect.top = spacing;
        }
    }

}
