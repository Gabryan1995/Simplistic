package com.example.checklistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    ArrayList<Checklist> checklists;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    // Keys
    private static final int NEW_CHECKLIST_REQUEST_CODE = 0;
    private static final int EDITED_CHECKLIST_REQUEST_CODE = 1;

    private static final String FILE_NAME = "checklists.json";
    private static final String CHECKLIST_OBJECT_KEY = "checklists";
    private static final String TITLE_KEY = "title";

    public static final String INTENT_TITLE_KEY = "new_checklist_title";
    public static final String INTENT_CHECKBOXES_STATUS_KEY = "new_checklist_checkbox_status";
    public static final String INTENT_TASK_KEY = "new_checklist_tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.checklist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        checklists = new ArrayList<>();

        mainAdapter = new MainAdapter(MainActivity.this, checklists);
        recyclerView.setAdapter(mainAdapter);

        if (isFilePresent()) {
            loadData();
        }

        // TODO:
        // Handle accessing and editing an existing checklist.


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                                                                                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();

                Collections.swap(checklists, from, to);
                mainAdapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                checklists.remove(viewHolder.getAdapterPosition());
                mainAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });

        helper.attachToRecyclerView(recyclerView);

        // Handle creating a new Checklist.
        FloatingActionButton fab = findViewById(R.id.new_checklist_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, ChecklistActivity.class), NEW_CHECKLIST_REQUEST_CODE);
            }
        });

        // Get Search Query
        handleIntent(getIntent());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case NEW_CHECKLIST_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(INTENT_TITLE_KEY);
                    boolean[] checkboxesArray = data.getBooleanArrayExtra(INTENT_CHECKBOXES_STATUS_KEY);
                    ArrayList<Boolean> checkboxes = new ArrayList<>();
                    for (int i = 0; i < checkboxesArray.length; i++) {
                        checkboxes.add(checkboxesArray[i]);
                    }

                    ArrayList<String> task = data.getStringArrayListExtra(INTENT_TASK_KEY);

                    checklists.add(new Checklist(title, checkboxes, task));

                    mainAdapter.notifyDataSetChanged();
                }
            case EDITED_CHECKLIST_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    // TODO: HANDLE EDITED CHECKLIST SAVING

                }
        }

    }

    public void loadData() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray checklistArray = obj.getJSONArray(CHECKLIST_OBJECT_KEY);

            checklists.clear();

            for (int i = 0; i < checklistArray.length(); i++) {
                String title = checklistArray.getJSONObject(i).getString(TITLE_KEY);
                JSONArray checklistItemArray = checklistArray.getJSONArray(i);
                ArrayList<Boolean> checkboxes = new ArrayList<>();
                ArrayList<String> tasks = new ArrayList<>();

                for (int j = 0; j < checklistItemArray.length(); j++) {
                    checkboxes.add(checklistItemArray.getBoolean(j));
                    tasks.add(checklistItemArray.getString(j));
                }
                checklists.add(new Checklist(title, checkboxes, tasks));

                mainAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        try {
            FileInputStream fileInputStream = getApplicationContext().openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(currentLine);
            }
            return stringBuilder.toString();
        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }

    public boolean isFilePresent() {
        String path = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + FILE_NAME;
        File file = new File(path);
        return file.exists();
    }

    // ** Setting up Search Functionality ** //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Snackbar.make(findViewById(android.R.id.content).getRootView(), "Handle query: " + query, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }
    // ** Setting up Search Functionality ** //
}