package com.plaglabs.pillreminder.app.PillReminder;

import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.AlarmScheduler;
import com.plaglabs.pillreminder.app.R;
import com.plaglabs.pillreminder.app.Utils.DialogDate;
import com.plaglabs.pillreminder.app.Utils.DialogHour;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.PillReminder;

/**
 * Created by plagueis on 11/05/14.
 */
public class EveryHoursFragment extends Fragment {

    PillReminderDBHelper db;
    private PillReminder pillReminder;
    EditText etEveryHours;
    TextView tvHourStart;
    Button btnSave, btnCancel;
    private boolean tvHourStartHasTime=false;

    public EveryHoursFragment() {
    }

    public EveryHoursFragment(PillReminder pillReminder) {
        this.pillReminder = pillReminder;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_every_hours, container,false);

        etEveryHours = (EditText) view.findViewById(R.id.etEveryHours);

        tvHourStart = (TextView) view.findViewById(R.id.tvHourStart);

        tvHourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTimeSelect dialogTimeSelect = new DialogTimeSelect();
                dialogTimeSelect.show(getFragmentManager(), "dialog");
            }
        });

        SpannableString spanString = new SpannableString(tvHourStart.getText().toString());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);

        tvHourStart.setText(spanString);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new TypeSelectFragment(pillReminder))
                        .addToBackStack("Pills1")
                        .commit();
            }
        });


        btnSave = (Button) view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkNext()){
                    pillReminder.setmEveryHours(Integer.parseInt(etEveryHours.getText().toString()));
                    pillReminder.setMhourStart(tvHourStart.getText().toString());
                    // TODO Look for next Reminder ID
                    db = new PillReminderDBHelper(getActivity());
                    int newReminderId = db.getNextReminderId();
                    Log.e("Test", String.valueOf(newReminderId));
                    pillReminder.setmReminderId(newReminderId);
                    db.createPillReminder(pillReminder);

                    db.closeDB();

                    createAlarm();

                    Fragment fragment;
                    fragment = PillsReminderFragment.newInstance(PillReminder.STATE_ACTIVE);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame,fragment)
                            .addToBackStack("new")
                            .commit();
                } else {
                    Toast.makeText(getActivity(),"You must select the frequency of your pill and the start hour",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void createAlarm() {
        AlarmScheduler alarm = new AlarmScheduler();
        int reminderId = pillReminder.getmReminderId();
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int everyHour = Integer.parseInt(etEveryHours.getText().toString());

        String date = pillReminder.getmDateStart();

        year = Integer.parseInt(date.substring(6,10));
        month = Integer.parseInt(date.substring(3,5)) - 1;
        day = Integer.parseInt(date.substring(0,2));

        String time = pillReminder.getMhourStart();

        hour = Integer.parseInt(time.substring(0,2));
        minute = Integer.parseInt(time.substring(3,5));
        Toast.makeText(getActivity(),String.valueOf(reminderId),Toast.LENGTH_SHORT).show();
        alarm.scheduleAlarm(getActivity(),reminderId,year,month,day,hour,minute,everyHour);
    }

    private boolean checkNext() {
        boolean next = false;

        if(tvHourStartHasTime && !etEveryHours.getText().toString().trim().equalsIgnoreCase("")){
            next = true;
        }

        return next;
    }

    private class DialogTimeSelect extends DialogHour {

        int code;

        private DialogTimeSelect() {
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            changeTime(hourOfDay,minute);
        }
    }
    private void changeTime(int hourOfDay, int minute) {
        String min;

        if (minute< 10){
            min = "0" + String.valueOf(minute);
        } else {
            min = String.valueOf(minute);
        }

        String date = String.valueOf(hourOfDay) + ":" +  min;
        SpannableString spanString = new SpannableString(date);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        tvHourStart.setText(spanString);
        tvHourStartHasTime = true;
    }
}
