package com.guidewire.microtodolist;

public class Task {

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
