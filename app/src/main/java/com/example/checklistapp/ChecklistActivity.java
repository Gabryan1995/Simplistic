package com.example.checklistapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.checklistapp.MainActivity.INTENT_TITLE_KEY;
import static com.example.checklistapp.MainActivity.INTENT_CHECKBOXES_STATUS_KEY;
import static com.example.checklistapp.MainActivity.INTENT_TASK_KEY;

public class ChecklistActivity extends AppCompatActivity {

    // TODO: HANDLE CHECKLIST ACTIVITY
    // - Send Checklist back to MainActivity
    // - Handle setting up a new checklist, or taking data to edit an existing checklist.

    private Checklist checklist;
    private EditText title;
    RecyclerView recyclerView;
    ChecklistAdapter checklistAdapter;

    ChecklistActivity(Checklist selectedChecklist) {
        checklist = selectedChecklist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        title = findViewById(R.id.checklist_title);

        recyclerView = (RecyclerView) findViewById(R.id.checklist_item_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checklistAdapter = new ChecklistAdapter(ChecklistActivity.this);
        recyclerView.setAdapter(checklistAdapter);

        if (checklist != null) {

            for (int i = 0; i < checklist.getCheckboxes().size(); i++) {

            }
        } else {

        }
    }

    public void returnChecklist() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(INTENT_TITLE_KEY, checklist.getTitle());
        returnIntent.putExtra(INTENT_CHECKBOXES_STATUS_KEY, checklist.getCheckboxes());
        returnIntent.putExtra(INTENT_TASK_KEY, checklist.getTasks());
        setResult(RESULT_OK, returnIntent);
    }

}
