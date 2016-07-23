package com.bryant.microtodolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.facebook.FacebookSdk;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class Main extends ActionBarActivity {
    //protected ArrayList<String> itemTexts;
    //protected ArrayAdapter<String> listAdapter;
    protected TaskArrayAdaptor adapter;
    protected ArrayList<Task> tasks;
    public static final int REQUEST_NEW_TASK = 1;
    public static final int VIEW_SETTINGS = 2;


    SharedPreferences mPrefs;
    SharedPreferences settings;
    static final Type TASK_LIST_TYPE = new TypeToken<ArrayList<Task>>(){}.getType();
    int position = 0;
    ListView listView;
    ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: ORGANIZE THIS (onCreate) INTO SEPARATE FUNCTIONS!


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mPrefs = getSharedPreferences("prefFile", MODE_PRIVATE);

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        String json = mPrefs.getString("tasks", "");
        tasks = new ArrayList<>();


        if (!json.equals("")) {
            tasks = gson.fromJson(json, TASK_LIST_TYPE);
        }

        adapter = new TaskArrayAdaptor(this, tasks);
        listView = (ListView)findViewById(R.id.taskList);


        // Swipe to delete
        // Adapted from http://nisostech.com/swipe-listview-android-example/
        listView.setOnTouchListener(new OnSwipeTouchListener(this, listView) {

            @Override
            public void onSwipeRight(int pos) {

                Toast.makeText(Main.this, "right swipe action here", Toast.LENGTH_LONG).show();
                showDeleteButton(pos);

                // Archive it!
                // TODO: do action instead of showing Toast
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(Main.this, "left swipe action here", Toast.LENGTH_LONG).show();
            }
        });
        // btn.setOnClickListener(listener);
        /** Setting the adapter to the ListView */
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

        if (settings.getBoolean("show_countdown", true)) {
            drawTimeLeft();
        }

    }


    private boolean showDeleteButton(int pos) {
        position = pos;
        View child = listView.getChildAt(pos - listView.getFirstVisiblePosition());
        if (child != null) {

            delete = (ImageView) child.findViewById(R.id.delete);
            if (delete != null) {
                if (delete.getVisibility() == View.INVISIBLE) {
                    Animation animation =
                            AnimationUtils.loadAnimation(this,
                                    R.anim.slide_out_left);
                    delete.startAnimation(animation);
                    delete.setVisibility(View.VISIBLE);
                } else {
                    Animation animation =
                            AnimationUtils.loadAnimation(this,
                                    R.anim.slide_in_right);
                    delete.startAnimation(animation);
                    delete.setVisibility(View.INVISIBLE);
                }
            }
            return true;
        }
        return false;
    }


    /**
     * Refreshes the timer countdown
     */
    protected void drawTimeLeft() {
        final TextView countdown_textview = (TextView)findViewById(R.id.countdown_text);
        String working_start_time = settings.getString("working_start_time", "08:00");
        String working_end_time = settings.getString("working_end_time", "20:00");

        int working_start_hour = TimePreference.getHour(working_start_time);
        int working_start_minute = TimePreference.getMinute(working_start_time);
        int working_end_hour = TimePreference.getHour(working_end_time);
        int working_end_minute = TimePreference.getMinute(working_end_time);

        // TODO: check if there is a one-liner to find the seconds left until time
        SimpleDateFormat hour = new SimpleDateFormat("H", Locale.US);
        SimpleDateFormat minute = new SimpleDateFormat("mm", Locale.US);
        SimpleDateFormat second = new SimpleDateFormat("ss", Locale.US);
        SimpleDateFormat hour_minutes = new SimpleDateFormat("HH:mm", Locale.US);
        String current_time = hour_minutes.format(new Date());
        int current_hour = Integer.parseInt(hour.format(new Date()));


        // TODO: Get time from settings instead of assuming 08:00-20:00 day
        // TODO: handle in function
        // TODO: handle when working_end_time is before working_start_time
        if (TimePreference.getTimeValue(current_time) >= TimePreference.getTimeValue(working_start_time) &&
                TimePreference.getTimeValue(current_time) < TimePreference.getTimeValue(working_end_time)) {
            int hoursLeft = (working_end_hour - 1) - current_hour;
            int tmp_minutes = working_end_minute == 0 ? 59 : working_end_minute - 1;
            int minutesLeft = tmp_minutes - Integer.parseInt(minute.format(new Date()));
            int secondsLeft = 60 - Integer.parseInt(second.format(new Date()));

            int millisecondsLeft = 1000 * (secondsLeft + 60 * minutesLeft + 3600 * hoursLeft);

            CountDownTimer cT =  new CountDownTimer(millisecondsLeft, 1000) {
                public void onTick(long millisUntilFinished) {
                    String hh = String.format(Locale.US, "%02d", millisUntilFinished/3600000);
                    String mm = String.format(Locale.US, "%02d", (int)((millisUntilFinished % 3600000) / 60000));
                    String ss = String.format(Locale.US, "%02d", (int)((millisUntilFinished % 60000) / 1000));
                    String timeText = hh + "HR " + mm + "MIN " + ss + "S";
                    countdown_textview.setText(timeText);
                }

                public void onFinish() {
                    countdown_textview.setText("Done!");
                }
            };
            cT.start();
        }
        else {
            countdown_textview.setText(R.string.done_with_day);
        }
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
        prefsEditor.apply();
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, VIEW_SETTINGS);
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

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_NEW_TASK) {
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
            else if (requestCode == VIEW_SETTINGS) {
                loadUserSettings();
            }
        }
        else {
            Log.d("onActivityResult", "requestCode does not equal RESULT_OK!!");
        }

    }

    protected void loadUserSettings() {
        // TODO: update settings
        Log.d("LOAD_SETTINGS", "TODO!!");
    }
}
