package com.example.alasdairwilkins.calendarmobile;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

abstract class EventSuperClass extends TimeManipulationSuperClass {
    public TextView activityHeader;
    public EditText eventTitle;
    public EditText eventDescription;
    public TextView startDateTextView;
    public DatePickerDialog.OnDateSetListener startDateSetListener;
    public TextView startTimeTextView;
    public TimePickerDialog.OnTimeSetListener startTimeSetListener;
    public TextView endDateTextView;
    public DatePickerDialog.OnDateSetListener endDateSetListener;
    public TextView endTimeTextView;
    public TimePickerDialog.OnTimeSetListener endTimeSetListener;
    public CheckBox allDayCheckBox;
    public Button submitButton;
    private String TAG = "EventSuperClass";

    public Calendar startCalendar = Calendar.getInstance();
    public Calendar endCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        activityHeader = (TextView) findViewById(R.id.activityHeader);

        eventTitle = (EditText) findViewById(R.id.eventTitle);
        eventTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                checkSubmit();
            }
        });

        eventDescription = (EditText) findViewById(R.id.eventDescription);

        startDateTextView = (TextView) findViewById(R.id.startDate);
        startTimeTextView = (TextView) findViewById(R.id.startTime);
        endDateTextView = (TextView) findViewById(R.id.endDate);
        endTimeTextView = (TextView) findViewById(R.id.endTime);


        allDayCheckBox = (CheckBox) findViewById(R.id.allDay);

        allDayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    startTimeTextView.setEnabled(false);
                    endTimeTextView.setEnabled(false);
                    if (startTimeTextView.getText() == "") {startTimeTextView.setText("N/A");}
                    if (endTimeTextView.getText() == "") {endTimeTextView.setText("N/A");}
                } else {
                    startTimeTextView.setEnabled(true);
                    endTimeTextView.setEnabled(true);
                    if (startTimeTextView.getText() == "N/A") {startTimeTextView.setText("");}
                    if (endTimeTextView.getText() == "N/A") {endTimeTextView.setText("");}
                }
            }
        });

        startDateTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = makeDatePickerDialog(startCalendar, startDateSetListener);
                dialog.show();
            }

        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                updateDate(startCalendar, endCalendar, startDateTextView, endDateTextView,
                        endTimeTextView, year, month, day, true);

                checkSubmit();

            }
        };

        startTimeTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                TimePickerDialog dialog = makeTimePickerDialog(startCalendar, startTimeSetListener);
                dialog.show();

            }

        });

        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                updateTime(startCalendar, endCalendar, startTimeTextView, endTimeTextView,
                        endDateTextView, hour, minute, true);
                checkSubmit();

            }
        };

        endDateTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = makeDatePickerDialog(endCalendar, endDateSetListener);

                if (startDateTextView.getText() != "") {
                    dialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis());
                }

                dialog.show();

            }

        });

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                updateDate(endCalendar, startCalendar, endDateTextView, startDateTextView,
                        startTimeTextView, year, month, day, false);

                checkSubmit();

            }
        };

        endTimeTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                TimePickerDialog dialog = makeTimePickerDialog(endCalendar, endTimeSetListener);
                dialog.show();

            }

        });

        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {

                updateTime(endCalendar, startCalendar, endTimeTextView, startTimeTextView,
                        startDateTextView, hour, minute, false);

                checkSubmit();
            }
        };


    }

    public void checkSubmit() {
        if (submitValid()) {
            submitButton.setEnabled(true);
        } else {
            submitButton.setEnabled(false);
        }
    }

    public boolean submitValid() {
        if (eventTitle.getText().toString().length() != 0 && startDateTextView.getText() != "" && endDateTextView.getText() != "") {
            if (!allDayCheckBox.isChecked()) {
                if (startTimeTextView.getText() != "" && endTimeTextView.getText() != "") {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public DatePickerDialog makeDatePickerDialog(Calendar calendar, DatePickerDialog.OnDateSetListener dateSetListener) {
        DatePickerDialog dialog = new DatePickerDialog(
                EventSuperClass.this,
                android.R.style.Theme_DeviceDefault,
                dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public TimePickerDialog makeTimePickerDialog(Calendar calendar, TimePickerDialog.OnTimeSetListener timeSetListener) {
        TimePickerDialog dialog = new TimePickerDialog(
                EventSuperClass.this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public void updateDate(Calendar updatedCalendar, Calendar otherCalendar, TextView updatedDateTextView,
                           TextView otherDateTextView, TextView otherTimeTextView, int year, int month, int day, boolean isStart) {

        updatedCalendar.set(year, month, day);

        if (isStart ? updatedCalendar.after(otherCalendar) : otherCalendar.after(updatedCalendar)) {
            otherCalendar.set(year, month, day,
                    (isStart ? updatedCalendar.get(Calendar.HOUR_OF_DAY) + 1 : updatedCalendar.get(Calendar.HOUR_OF_DAY) - 1),
                    updatedCalendar.get(Calendar.MINUTE));

            if (otherTimeTextView.getText() != timeString(otherCalendar)) {
                otherTimeTextView.setText(timeString(otherCalendar));
            }

        }

        updatedDateTextView.setText(dateString(updatedCalendar));
        otherDateTextView.setText(dateString(otherCalendar));

    }

    public void updateTime(Calendar updatedCalendar, Calendar otherCalendar, TextView updatedTimeTextView,
                           TextView otherTimeTextView, TextView otherDateTextView, int hour, int minute, boolean isStart) {

        updatedCalendar.set(Calendar.HOUR_OF_DAY, hour);
        updatedCalendar.set(Calendar.MINUTE, minute);

        updatedTimeTextView.setText(timeString(updatedCalendar));

        if (isStart ? updatedCalendar.after(otherCalendar) : otherCalendar.after(updatedCalendar)) {
            otherCalendar.set(Calendar.HOUR_OF_DAY,
                    (isStart ? updatedCalendar.get(Calendar.HOUR_OF_DAY) + 1 : updatedCalendar.get(Calendar.HOUR_OF_DAY) - 1));
            otherCalendar.set(Calendar.MINUTE, updatedCalendar.get(Calendar.MINUTE));
            if (otherTimeTextView.getText() == "") {
                otherTimeTextView.setHint(timeString(otherCalendar));
            } else {
                otherTimeTextView.setText(timeString(otherCalendar));
            }
            if (otherDateTextView.getText() != dateString(otherCalendar)) {
                otherDateTextView.setText(dateString(otherCalendar));
            }

        }
    }


}
