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
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    ArrayList<Checklist> checklists;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    // Keys
    public static final int CHECKLIST_REQUEST_CODE = 0;
    public static final String PARCELABLE_KEY = "parcelable_obj";
    public static final String POSITION_KEY = "position";

    private static final String FILE_NAME = "checklists.json";
    private static final String TITLE_KEY = "title";
    private static final String CHECKBOXES_KEY = "checkboxes";
    private static final String TASK_KEY = "tasks";

    public static final String INTENT_TITLE_KEY = "new_checklist_title";
    public static final String INTENT_CHECKBOXES_STATUS_KEY = "new_checklist_checkbox_status";
    public static final String INTENT_TASK_KEY = "new_checklist_tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checklists = new ArrayList<>();

        if (isFilePresent()) {
            loadData();
        }

        recyclerView = findViewById(R.id.checklist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainAdapter = new MainAdapter(MainActivity.this, checklists);
        recyclerView.setAdapter(mainAdapter);

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
                startActivityForResult(new Intent(MainActivity.this, ChecklistActivity.class), CHECKLIST_REQUEST_CODE);
            }
        });

        // Get Search Query
        handleIntent(getIntent());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHECKLIST_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(INTENT_TITLE_KEY);
                    boolean[] checkboxesArray = data.getBooleanArrayExtra(INTENT_CHECKBOXES_STATUS_KEY);
                    ArrayList<Boolean> checkboxes = new ArrayList<>();

                    for (int i = 0; i < checkboxesArray.length; i++) {
                        checkboxes.add(checkboxesArray[i]);
                    }
                    ArrayList<String> task = data.getStringArrayListExtra(INTENT_TASK_KEY);

                    if (data.hasExtra(MainActivity.POSITION_KEY)) {
                        checklists.set(data.getIntExtra(POSITION_KEY, 0), new Checklist(title, checkboxes, task));
                    } else {
                        checklists.add(new Checklist(title, checkboxes, task));
                    }
                    mainAdapter.notifyDataSetChanged();
                }
            }
        }

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
            
        }
    }
    // ** Setting up Search Functionality ** //

    private void saveData() {
        // Creating JSONArray to store in file.
        JSONArray checklistsArray = new JSONArray();

        try {
            for (int i = 0; i < checklists.size(); i++) {
                JSONObject checklistObj = new JSONObject();

                JSONArray checkboxesArray = new JSONArray();
                JSONArray tasksArray = new JSONArray();

                for (int j = 0; j < checklists.get(i).getCheckboxes().size(); j++) {
                    checkboxesArray.put(checklists.get(i).getCheckboxes().get(j));
                    tasksArray.put(checklists.get(i).getTasks().get(j));
                }

                checklistObj.put(TITLE_KEY, checklists.get(i).getTitle());
                checklistObj.put(CHECKBOXES_KEY, checkboxesArray);
                checklistObj.put(TASK_KEY, tasksArray);

                checklistsArray.put(checklistObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            File file = new File(getApplicationContext().getFilesDir(), FILE_NAME);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(checklistsArray.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadData() {
        File file = new File(getApplicationContext().getFilesDir(), FILE_NAME);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String response = stringBuilder.toString();

            JSONArray checklistsArray = new JSONArray(response);

            for (int i = 0; i < checklistsArray.length(); i++) {
                JSONObject currentChecklist = (JSONObject) checklistsArray.get(i);

                String title = (String) currentChecklist.get(TITLE_KEY);

                JSONArray checkboxesArray = (JSONArray) currentChecklist.get(CHECKBOXES_KEY);
                JSONArray tasksArray = (JSONArray) currentChecklist.get(TASK_KEY);
                ArrayList<Boolean> checkboxes = new ArrayList<>();
                ArrayList<String> tasks = new ArrayList<>();

                for (int j = 0; j < checkboxesArray.length(); j++) {
                    checkboxes.add(checkboxesArray.getBoolean(j));
                    tasks.add(tasksArray.getString(j));
                }
                checklists.add(new Checklist(title, checkboxes, tasks));
            }
        } catch (FileNotFoundException e) {
            //Your exception handling here
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isFilePresent() {
        String path = getApplicationContext().getFilesDir() + "/" + FILE_NAME;
        File file = new File(path);
        return file.exists();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }
}