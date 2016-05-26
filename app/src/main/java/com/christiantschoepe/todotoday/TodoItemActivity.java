package com.christiantschoepe.todotoday;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TodoItemActivity extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    public final static String EXTRA_ID = "todolist.ITEM_ID";
    public final static String EXTRA_TITLE = "todolist.ITEM_TITLE";
    public final static String EXTRA_DESCRIPTION = "todolist.ITEM_DESCRIPTION";
    public final static String EXTRA_DUE_DATE = "todolist.ITEM_DUE_DATE";
    public final static String EXTRA_DUE_TIME = "todolist.ITEM_DUE_TIME";

    EditText itemTitle;
    EditText itemDescription;
    EditText itemDueDate;
    EditText itemDueTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemTitle = (EditText) findViewById(R.id.todo_item_title);
        itemDescription = (EditText) findViewById(R.id.todo_item_description);
        itemDueDate = (EditText) findViewById(R.id.todo_item_date);
        itemDueTime = (EditText) findViewById(R.id.todo_item_time);

        Intent intent = getIntent();
        final int id = intent.getIntExtra(EXTRA_ID, -1);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String desc = intent.getStringExtra(EXTRA_DESCRIPTION);
        String dueDate = intent.getStringExtra(EXTRA_DUE_DATE);
        String dueTime = intent.getStringExtra(EXTRA_DUE_TIME);
        boolean isNewItem = title == null;
        if(title != null) {
            itemTitle.setText(title);
            itemDescription.setText(desc);
            itemDueDate.setText(dueDate);
            itemDueTime.setText(dueTime);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoItemActivity.this, TodoListActivity.class);
                String title = itemTitle.getText().toString();
                String desc = itemDescription.getText().toString();
                String dueDate = itemDueDate.getText().toString();
                String dueTime = itemDueTime.getText().toString();

                if(title.trim().equals("")) {
                    Snackbar.make(view, "Please put a valid title", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    intent.putExtra(EXTRA_ID, id);
                    intent.putExtra(EXTRA_TITLE, title);
                    intent.putExtra(EXTRA_DESCRIPTION, desc);
                    intent.putExtra(EXTRA_DUE_TIME, dueTime);
                    intent.putExtra(EXTRA_DUE_DATE, dueDate);
//                    startActivity(intent);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        TextView title = new TextView(this);
//        title.setText("My item title");
//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.todo_content);
//        layout.addView(title);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
//        itemDueTime.setText("" + (hourOfDay % 12 == 0 ? "12" : hourOfDay % 12) + ":" + minute + (hourOfDay >= 12 ? "pm" : "am"));
        itemDueTime.setText("" + hourOfDay + ":" + minute);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        itemDueDate.setText("" + month + "/" + day + "/" + year);
    }

    public static class TimePickerFragment extends DialogFragment {

        TimePickerDialog.OnTimeSetListener mListener;

        public TimePickerFragment() { /* Do not instantiate */  }

        public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener) {
            super();
            mListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), mListener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

    public static class DatePickerFragment extends DialogFragment {

        DatePickerDialog.OnDateSetListener mListener;

        public DatePickerFragment() { /* Do ont instantiate */ }

        public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
            super();
            mListener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), mListener, year, month, day);
        }
    }

}
