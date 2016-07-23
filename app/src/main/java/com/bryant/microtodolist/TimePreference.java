package com.bryant.microtodolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;


/**
 * Based on solutions from stackoverflow
 * http://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen
 */
public class TimePreference extends DialogPreference {
    protected int lastHour = 0;
    protected int lastMinute = 0;
    protected TimePicker picker = null;
    // protected SharedPreferences mPrefs;

    // TODO: handle/throw error on bad time input
    // TODO: test this class

    public static int getHour(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces = time.split(":");

        return (Integer.parseInt(pieces[1]));
    }

    /**
     * The time value is the number of minutes since 00:00, ie: hours * 60 + minutes.
     * @param time - In the form of "HH:MM"
     * @return Time value of a 24-hour time.
     */
    public static int getTimeValue(String time) {
        return 60 * getHour(time) + getMinute(time);
    }

    /**
     * Checks whether a time is between two designated times
     * @param time
     * @param start
     * @param end
     * @param inclusive If `true`, the function will return `true` if `time` is equal to either
     *                  `start` or `end`.
     * @return
     */
    public static boolean isBetweenTimes(String time, String start, String end, boolean inclusive) {
        int timeVal = getTimeValue(time);
        int startVal = getTimeValue(start);
        int endVal = getTimeValue(end);

        if (inclusive && (timeVal == startVal || timeVal == endVal))
            return true;

        if (startVal <= endVal) {
            return timeVal > startVal && timeVal < endVal;
        }
        else {
            // example: is 1:00 in 23:00-4:00?
            // is 23:00 in 22:00-4:00?
            return timeVal > endVal || timeVal < startVal;
        }
    }


    public TimePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);

        // mPrefs = ctxt.getSharedPreferences("prefFile", Context.MODE_PRIVATE);

        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());

        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);

        picker.setCurrentHour(lastHour);
        picker.setCurrentMinute(lastMinute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            lastHour = picker.getCurrentHour();
            lastMinute = picker.getCurrentMinute();

            String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);

            setSummary(getSummary());
            if (callChangeListener(time)) {
                persistString(time);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time = null;

        if (restoreValue) {
            if (defaultValue == null) {
                time = getPersistedString("00:00");
            } else {
                time = getPersistedString(defaultValue.toString());
            }
        } else {
            time = defaultValue.toString();
        }

        lastHour = getHour(time);
        lastMinute = getMinute(time);

        setSummary(getSummary());
    }


    @Override
    public CharSequence getSummary() {
        String hourText = String.valueOf(lastHour);
        hourText = hourText.length() == 2 ? hourText : "0" + hourText;

        String minuteText = String.valueOf(lastMinute);
        minuteText = minuteText.length() == 2 ? minuteText : "0" + minuteText;

        return hourText + ":" + minuteText;
    }
}