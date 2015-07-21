package com.guidewire.microtodolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Main extends ActionBarActivity {
    //protected ArrayList<String> itemTexts;
    //protected ArrayAdapter<String> listAdapter;
    protected TaskArrayAdaptor adapter;
    protected ArrayList<Task> tasks;
    public static final int REQUEST_NEW_TASK = 1;
    SharedPreferences mPrefs;
    static final Type TASK_LIST_TYPE = new TypeToken<ArrayList<Task>>(){}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = getSharedPreferences("prefFile", 0);


        Gson gson = new Gson();
        String json = mPrefs.getString("tasks", "");
        tasks = new ArrayList<>();


        if (!json.equals("")) {
            tasks = gson.fromJson(json, TASK_LIST_TYPE);
        }

        adapter = new TaskArrayAdaptor(this, tasks);
        ListView listView = (ListView)findViewById(R.id.taskList);
        listView.setAdapter(adapter);
        /*
        itemTexts = new ArrayList<>();

        for (Task task : tasks) {
            itemTexts.add(task.toString());
        }

        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                itemTexts);
        ListView list = (ListView)findViewById(R.id.taskList);
        list.setAdapter(listAdapter);
        */



    }

    public void removeTask(Task task) {
        tasks.remove(task);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks, TASK_LIST_TYPE);
        prefsEditor.putString("tasks", json);
        prefsEditor.commit();
    }


    public void addTask(Task task) {
        //tasks.get(tasks.size()-1);
        tasks.add(task);
        //updateDisplay();
        //itemTexts.add(task.toString());
        //listAdapter.notifyDataSetChanged();

        adapter.notifyDataSetChanged();
    }

    /*
    public void updateDisplay() {

    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewTask(View view) {
        Log.d("CLICK_BUTTON", "Hi!!!");
        Intent intent = new Intent(this, AddTask.class);
        intent.putExtra("parent", "Main");
        startActivityForResult(intent, REQUEST_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_TASK && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            double time = data.getDoubleExtra("time", 30);

            if (data.hasExtra("day") && data.hasExtra("month") && data.hasExtra("year")) {
                int year = data.getIntExtra("year", 0);
                int month = data.getIntExtra("month", 0);
                int day = data.getIntExtra("day", 0);
                addTask(new Task(name, time, year, month, day));
            }
            else {
                addTask(new Task(name, time));
            }

        }
    }
}
