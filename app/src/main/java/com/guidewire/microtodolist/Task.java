package com.guidewire.microtodolist;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    private int mData;

    /**
     * default to 0, otherwise the timestamp that the task must be completed
     */
    long finishTime;

    /**
     * Amount of time the task takes
     */
    double time;
    /**
     * Task name
     */
    String name;

    /**
     * If the task is recurring, then the standard deviation from previous tasks can be useful
     */
    double std_dev;

    /**
     * true if the task can be split into different blocks of time
     */
    boolean splittable;

    /**
     * The higher the number, the higher the priority
     */
    int priority;

    boolean completed;

    boolean scheduled;
    protected enum TaskType {
        TASK, APPOINTMENT, GOAL
    }

    TaskType type;



    public Task(String name, double time) {
        this.name = name;
        this.time = time;

        finishTime = 0;
        // Just an estimate right now.  Eg: an hour of work is off by +/- 6 minutes with 68%
        // confidence.
        std_dev = 0.10 * time;
        splittable = true;
        priority = 0;
        scheduled = false;
        completed = false;
        type = TaskType.TASK;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mData);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private Task(Parcel in) {
        mData = in.readInt();
    }


    protected void aggregate_stats(double timeTaken) {
        // set time taken, update std_dev and mean of reoccuring activities
    }

    public void finish(double timeTaken) {
        aggregate_stats(timeTaken);
        completed = true;
    }


    @Override
    public String toString() {
        return name;
    }
}
