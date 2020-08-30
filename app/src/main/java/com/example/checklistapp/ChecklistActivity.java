package com.example.checklistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.checklistapp.MainActivity.INTENT_TITLE_KEY;
import static com.example.checklistapp.MainActivity.INTENT_CHECKBOXES_STATUS_KEY;
import static com.example.checklistapp.MainActivity.INTENT_TASK_KEY;

public class ChecklistActivity extends AppCompatActivity {

    // Data
    private Checklist checklist;

    // Components
    private EditText title;
    RecyclerView recyclerView;
    ChecklistAdapter checklistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        Intent intent = getIntent();
        if (intent.hasExtra(MainActivity.PARCELABLE_KEY)) {
            checklist = intent.getParcelableExtra(MainActivity.PARCELABLE_KEY);
        } else {
            checklist = new Checklist();
        }

        title = findViewById(R.id.checklist_title);
        title.setText(checklist.getTitle());

        recyclerView = findViewById(R.id.checklist_item_recyclerview);
        checklistAdapter = new ChecklistAdapter(checklist);

        recyclerView.setAdapter(checklistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Sends the checklist's data back to MainActivity to
     * be saved unless the title for the checklist is empty.
     */
    private void returnChecklist() {
        if (TextUtils.isEmpty(title.getText().toString())) {
            setResult(RESULT_CANCELED);
        } else {
            Intent returnIntent = new Intent();
            boolean[] tempCheckboxes = new boolean[checklist.getCheckboxes().size()];
            for (int i = 0; i < checklist.getCheckboxes().size(); i++) {
                tempCheckboxes[i] = checklist.getCheckboxes().get(i);
            }
            returnIntent.putExtra(INTENT_TITLE_KEY, title.getText().toString());
            returnIntent.putExtra(INTENT_CHECKBOXES_STATUS_KEY, tempCheckboxes);
            returnIntent.putExtra(INTENT_TASK_KEY, checklist.getTasks());

            if (getIntent().hasExtra(MainActivity.POSITION_KEY)) {
                returnIntent.putExtra(MainActivity.POSITION_KEY, getIntent().getIntExtra(MainActivity.POSITION_KEY, 0));
            }

            setResult(RESULT_OK, returnIntent);
        }
        finish();
    }

    /**
     * Inflates the menu to have the add new task button.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_checklist, menu);

        return true;
    }

    /**
     * Registers user's menu tap for either adding a new task to their
     * checklist, or going back to the parent activity (MainActivity).
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addtask:
                checklist.addEmptyTask();
                recyclerView.getAdapter().notifyItemInserted(checklist.size());
                recyclerView.smoothScrollToPosition(checklist.size());
                return true;
            case android.R.id.home:
                returnChecklist();
                return true;
        }
        return false;
    }

    /**
     * Returns the checklist in the case that the user
     * presses the back button instead of the up button.
     */
    @Override
    public void onBackPressed() {
        returnChecklist();
        super.onBackPressed();
    }
}
