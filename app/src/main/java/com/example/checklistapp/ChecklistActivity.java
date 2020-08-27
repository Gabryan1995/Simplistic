package com.example.checklistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.example.checklistapp.MainActivity.INTENT_TITLE_KEY;
import static com.example.checklistapp.MainActivity.INTENT_CHECKBOXES_STATUS_KEY;
import static com.example.checklistapp.MainActivity.INTENT_TASK_KEY;

public class ChecklistActivity extends AppCompatActivity {

    // TODO: HANDLE CHECKLIST ACTIVITY
    // - Send Checklist back to MainActivity
    // - Handle taking data to edit an existing checklist.

    private Checklist checklist;

    private EditText title;

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
        title.setText(checklist.getTitle());


        recyclerView = findViewById(R.id.checklist_item_recyclerview);
        checklistAdapter = new ChecklistAdapter(checklist);

        recyclerView.setAdapter(checklistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void returnChecklist() {
        if (TextUtils.isEmpty(title.getText().toString())) {
            title.setError("Please enter a title for your checklist.");
        } else {
            Intent returnIntent = new Intent();
            boolean[] tempCheckboxes = new boolean[checklist.getCheckboxes().size()];
            for (int i = 0; i < checklist.getCheckboxes().size(); i++) {
                tempCheckboxes[i] = checklist.getCheckboxes().get(i);
            }
            returnIntent.putExtra(INTENT_TITLE_KEY, title.getText().toString());
            returnIntent.putExtra(INTENT_CHECKBOXES_STATUS_KEY, tempCheckboxes);
            returnIntent.putExtra(INTENT_TASK_KEY, checklist.getTasks());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
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
                recyclerView.getAdapter().notifyItemInserted(checklist.size());
                recyclerView.smoothScrollToPosition(checklist.size());
                return true;
            case android.R.id.home:
                returnChecklist();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        returnChecklist();
        super.onBackPressed();
    }
}
