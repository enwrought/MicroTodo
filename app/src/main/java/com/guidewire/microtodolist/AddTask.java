package com.guidewire.microtodolist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class AddTask extends ActionBarActivity {
    Calendar hardFinishDateCal = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            hardFinishDateCal.set(Calendar.YEAR, year);
            hardFinishDateCal.set(Calendar.MONTH, month);
            hardFinishDateCal.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Spinner spinner = (Spinner) findViewById(R.id.timeUnits);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_unit_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        EditText hardFinishDateView = (EditText)findViewById(R.id.hardFinishDate);
        hardFinishDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

    public void setDate(View view) {
        new DatePickerDialog(this, date, hardFinishDateCal.get(Calendar.YEAR),
                hardFinishDateCal.get(Calendar.MONTH),
                hardFinishDateCal.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        EditText hardFinishDateView = (EditText)findViewById(R.id.hardFinishDate);
        hardFinishDateView.setText(sdf.format(hardFinishDateCal.getTime()));
        
    }

    /**
     * Checks if entered information is valid and sends back to main activity to create a task
     * @param view
     */
    public void finishAddingTask(View view) {
        boolean everythingOK = true;
        Intent intent = new Intent();

        // Task name
        EditText nameView = (EditText)findViewById(R.id.taskName);
        if (nameView.getText().toString().isEmpty()) {
            everythingOK = false;
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Invalid task name");
            alertDialog.setMessage("Tasks cannot be empty.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        else {
            intent.putExtra("name", nameView.getText().toString());
        }

        // Task time
        EditText timeView = (EditText) findViewById(R.id.time);
        try {
            // TODO: Get global POINT_VALUE, std_dev, etc
            double POINT_VALUE = 180;
            double amount = Double.parseDouble(timeView.getText().toString());
            double multiplier;
            Spinner timeUnits = (Spinner) findViewById(R.id.timeUnits);
            switch(timeUnits.getSelectedItemPosition()) {
                case 0:
                    multiplier = 1;
                    break;
                case 1:
                    multiplier = 60;
                    break;
                case 2:
                    multiplier = POINT_VALUE;
                    break;
                default:
                    multiplier = 1;
            }
            intent.putExtra("time", amount * multiplier);
        }
        catch(NumberFormatException e) {
            everythingOK = false;
            Log.d("NumberFormatException", "Float not accepted");
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Invalid amount of time");
            alertDialog.setMessage("The amount of time for the task must be a number.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }


        // TODO: Add all entered fields
        EditText hardFinishDateView = (EditText)findViewById(R.id.hardFinishDate);
        if (hardFinishDateView.getText().toString().isEmpty()) {
            Log.d("STRING_EMPTY", "hardFinishDate is empty");
        }
        else {
            // Pass in date
            intent.putExtra("year", hardFinishDateCal.get(Calendar.YEAR));
            // To get the correct month, add 1
            intent.putExtra("month", hardFinishDateCal.get(Calendar.MONTH) + 1);
            intent.putExtra("day", hardFinishDateCal.get(Calendar.DAY_OF_MONTH));

        }

        if (everythingOK) {
            setResult(RESULT_OK, intent);
            finish();
        }



        // TODO: if pressed back, setResult to RESULT_CANCELED
    }
    

}
