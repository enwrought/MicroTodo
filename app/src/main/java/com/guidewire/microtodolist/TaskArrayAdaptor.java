package com.guidewire.microtodolist;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskArrayAdaptor extends ArrayAdapter<Task> {
    public static final int[] palette = {
            Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED, Color.BLUE, Color.MAGENTA
    };
    final float scale = getContext().getResources().getDisplayMetrics().density;
    public TaskArrayAdaptor(Context context, ArrayList<Task> tasks){
        super(context, 0, tasks);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);
        final Task taskRef = task;
        float textSize = 10 * scale;
        int timeSize = (int) (scale * task.getMinutes() + textSize);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.task_item, parent, false);
        }

        // Use as space if placeholder
        if (task.isPlaceHolder()) {
            Space space = (Space)convertView.findViewById(R.id.spacer);
            space.setMinimumHeight((int)(timeSize * scale));
            return convertView;
        }

        TextView taskName = (TextView)convertView.findViewById(R.id.taskName);
        TextView time = (TextView)convertView.findViewById(R.id.time);
        TextView finishDate = (TextView)convertView.findViewById(R.id.hardFinishDate);

        // Strikethrough if done
        if (task.isCompleted()) {
            taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        taskName.setText(task.getName());
        time.setText(Math.round(task.getMinutes()) + " min");

        if (task.getYear() > 0) {
            finishDate.setText(task.getFinishDate());
        }

        if (taskName.getMinimumHeight() < timeSize * scale)
            taskName.setHeight((int)(scale * timeSize));

        convertView.setBackgroundColor(palette[position % palette.length]);

        Button editButton = (Button)convertView.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start activity with button
                Log.d("EDIT_BUTTON", "Not yet implemented.");
            }
        });

        Button deleteButton = (Button)convertView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // remove task from tasks
                ((Main)getContext()).removeTask(taskRef);
            }
        });

        Button doneButton = (Button)convertView.findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            private Task task;
            public View.OnClickListener setTask(Task task) {
                this.task = task;
                return this;
            }
            @Override
            public void onClick(View v) {
                // Set completed
                task.complete();
                notifyDataSetChanged();
            }
        }.setTask(task));

        return convertView;
    }
}
