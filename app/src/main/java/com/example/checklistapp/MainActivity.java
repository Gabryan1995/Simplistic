package com.example.checklistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Data
    ArrayList<Checklist> checklists;

    // Components
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    SearchView searchView;

    public static final int CHECKLIST_REQUEST_CODE = 0;

    // JSON Keys
    private static final String FILE_NAME = "checklists.json";
    private static final String TITLE_KEY = "title";
    private static final String CHECKBOXES_KEY = "checkboxes";
    private static final String TASK_KEY = "tasks";

    // Intent Keys
    public static final String PARCELABLE_KEY = "parcelable_obj";
    public static final String POSITION_KEY = "position";
    public static final String INTENT_TITLE_KEY = "new_checklist_title";
    public static final String INTENT_CHECKBOXES_STATUS_KEY = "new_checklist_checkbox_status";
    public static final String INTENT_TASK_KEY = "new_checklist_tasks";

    // Shared Preferences - Theme
    public static int themeSelection;
    public static int currentPrimaryColor;
    public static int currentSecondaryColor;
    private SharedPreferences preferences;
    public String sharedPrefFileName = "com.example.android.checklistappsharedprefs";
    public static final String THEME_KEY = "theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences(sharedPrefFileName, MODE_PRIVATE);
        themeSelection = preferences.getInt(THEME_KEY, 0);
        switch (themeSelection) {
            case 0:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.red);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkRed);
                break;
            case 1:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.orange);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkOrange);
                break;
            case 2:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.yellow);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkYellow);
                break;
            case 3:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.green);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkGreen);
                break;
            case 4:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.blue);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkBlue);
                break;
            case 5:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.purple);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkPurple);
                break;
            case 6:
                currentPrimaryColor = ContextCompat.getColor(this, R.color.pink);
                currentSecondaryColor = ContextCompat.getColor(this, R.color.darkPink);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(currentPrimaryColor));

        checklists = new ArrayList<>();

        recyclerView = findViewById(R.id.checklist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setBackgroundColor(currentSecondaryColor);

        mainAdapter = new MainAdapter(MainActivity.this, checklists, currentPrimaryColor);
        recyclerView.setAdapter(mainAdapter);

        if (isFilePresent()) {
            loadData();
        }

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            int currentPosition = -1;
            int destPosition = -1;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (currentPosition == -1)
                    currentPosition = fromPosition;
                destPosition = toPosition;

                checklists.add(toPosition, checklists.remove(fromPosition));
                mainAdapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (currentPosition != -1 && destPosition != -1 && currentPosition != destPosition) {
                    if (currentPosition > destPosition)
                        mainAdapter.notifyItemRangeChanged(destPosition, currentPosition);
                    else
                        mainAdapter.notifyItemRangeChanged(currentPosition, destPosition);
                }

                currentPosition = destPosition = -1;
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
        fab.setBackgroundTintList(ColorStateList.valueOf(currentPrimaryColor));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, ChecklistActivity.class), CHECKLIST_REQUEST_CODE);
            }
        });
    }

    /**
     * Used to retrieve and update our checklist data after either
     * creating a new checklist or editing a preexisting checklist.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CHECKLIST_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra(INTENT_TITLE_KEY);
                    boolean[] checkboxesArray = data.getBooleanArrayExtra(INTENT_CHECKBOXES_STATUS_KEY);
                    ArrayList<Boolean> checkboxes = new ArrayList<>();

                    for (boolean checkbox : checkboxesArray) {
                        checkboxes.add(checkbox);
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

    /**
     * Sets up the SearchView for querying whatever text the user inputs.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mainAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                mainAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    /**
     * Registers the user's click when selecting the search button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                return true;
            case R.id.action_theme:
                showThemeSelectionDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showThemeSelectionDialog() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(THEME_KEY, 0);
        preferencesEditor.apply();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Themes")
                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor preferencesEditor = preferences.edit();
                        switch (which) {
                            case 0:
                                preferencesEditor.putInt(THEME_KEY, 0);
                                break;
                            case 1:
                                preferencesEditor.putInt(THEME_KEY, 1);
                                break;
                            case 2:
                                preferencesEditor.putInt(THEME_KEY, 2);
                                break;
                            case 3:
                                preferencesEditor.putInt(THEME_KEY, 3);
                                break;
                            case 4:
                                preferencesEditor.putInt(THEME_KEY, 4);
                                break;
                            case 5:
                                preferencesEditor.putInt(THEME_KEY, 5);
                                break;
                            case 6:
                                preferencesEditor.putInt(THEME_KEY, 6);
                                break;
                        }
                        preferencesEditor.apply();

                        finish();
                        startActivity(getIntent());
                    }
                });
        builder.show();
    }

    /**
     * Saves the users theme preferences
     */
    private void savePreferences() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        switch (currentPrimaryColor) {
            case R.color.red:
                preferencesEditor.putInt(THEME_KEY, 0);
                break;
            case R.color.orange:
                preferencesEditor.putInt(THEME_KEY, 1);
                break;
            case R.color.yellow:
                preferencesEditor.putInt(THEME_KEY, 2);
                break;
            case R.color.green:
                preferencesEditor.putInt(THEME_KEY, 3);
                break;
            case R.color.blue:
                preferencesEditor.putInt(THEME_KEY, 4);
                break;
            case R.color.purple:
                preferencesEditor.putInt(THEME_KEY, 5);
                break;
            case R.color.pink:
                preferencesEditor.putInt(THEME_KEY, 6);
                break;
        }
        preferencesEditor.apply();
    }

    /**
     * Used to store the ArrayList of Checklist objects to a JSON file.
     */
    private void saveData() {
        JSONArray checklistsArray = new JSONArray();

        try {
            for (Checklist checklist : checklists) {
                JSONObject checklistObj = new JSONObject();

                JSONArray checkboxesArray = new JSONArray();
                JSONArray tasksArray = new JSONArray();

                for (int j = 0; j < checklist.getCheckboxes().size(); j++) {
                    checkboxesArray.put(checklist.getCheckboxes().get(j));
                    tasksArray.put(checklist.getTasks().get(j));
                }

                checklistObj.put(TITLE_KEY, checklist.getTitle());
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

    /**
     * Used to load the user's Checklists from a JSON file.
     */
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

            checklists.clear();

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
                mainAdapter.notifyDataSetChanged();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    /**
     * Saves the user's checklists anytime the application is paused.
     */
    @Override
    public void onPause() {
        super.onPause();

        savePreferences();

        saveData();
    }
}