package com.plaglabs.pillreminder.app.PillReminder;

import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.plaglabs.pillreminder.app.AlarmScheduler;
import com.plaglabs.pillreminder.app.R;
import com.plaglabs.pillreminder.app.Utils.DialogConfirmation;
import com.plaglabs.pillreminder.app.Utils.DialogDate;
import com.plaglabs.pillreminder.app.Utils.DialogHour;

import java.util.ArrayList;
import java.util.List;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.PillReminder;
import SQLite.Model.Pill_PillReminder;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

/**
 * Created by plagueis on 10/05/14.
 */
public class PillReminderFragment extends Fragment {

    Pill_PillReminder mPillPillReminder;
    PillReminderDBHelper db;
    private int pillReminderId;

    ImageView imgPill;
    TextView tvPillName, tvDateStart, tvDateFinish, tvHourStart;
    EditText etEveryHours,etDescription;

    public static PillReminderFragment newInstance(int pillReminderId) {
        PillReminderFragment frag = new PillReminderFragment();
        Bundle args = new Bundle();
        args.putInt("pillReminderId", pillReminderId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pillReminderId = getArguments().getInt("pillReminderId");
        setHasOptionsMenu(true);
        db = new PillReminderDBHelper(getActivity());
        mPillPillReminder = db.getPillReminderWithPill(pillReminderId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_reminder, container,false);

        imgPill = (ImageView) view.findViewById(R.id.imgPill);
        imgPill.setImageResource(mPillPillReminder.getPill().getmImage());

        tvPillName = (TextView) view.findViewById(R.id.tvPill);
        tvPillName.setText(mPillPillReminder.getPill().getmName());

        etDescription = (EditText) view.findViewById(R.id.etDescription);
        etDescription.setText(mPillPillReminder.getPillReminder().getmDescription());

        tvDateStart = (TextView) view.findViewById(R.id.tvDateStart);
        SpannableString spanString = new SpannableString(mPillPillReminder.getPillReminder().getmDateStart());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        tvDateStart.setText(spanString);

        tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelect dialogDateSelect = new DialogSelect(TypeSelectFragment.CODE_START);
                dialogDateSelect.show(getFragmentManager(), "dialog");
            }
        });

        tvDateFinish = (TextView) view.findViewById(R.id.tvDateFinish);
        SpannableString spanString2 = new SpannableString(mPillPillReminder.getPillReminder().getmDateFinish());
        spanString2.setSpan(new UnderlineSpan(), 0, spanString2.length(), 0);
        tvDateFinish.setText(spanString2);

        tvDateFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelect dialogDateSelect = new DialogSelect(TypeSelectFragment.CODE_FINISH);
                dialogDateSelect.show(getFragmentManager(), "dialog");
            }
        });

        etEveryHours = (EditText) view.findViewById(R.id.tvEveryHours);
        etEveryHours.setText(String.valueOf(mPillPillReminder.getPillReminder().getmEveryHours()));

        tvHourStart = (TextView) view.findViewById(R.id.tvHourStart);
        SpannableString spanString3 = new SpannableString(mPillPillReminder.getPillReminder().getMhourStart());
        spanString3.setSpan(new UnderlineSpan(), 0, spanString3.length(), 0);
        tvHourStart.setText(spanString3);

        tvHourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTimeSelect dialogTimeSelect = new DialogTimeSelect();
                dialogTimeSelect.show(getFragmentManager(), "dialog");
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pills_reminder_menu_save_archive_restore_delete, menu);
        MenuItem item;
        switch (mPillPillReminder.getPillReminder().getmStatus()){
            case PillReminder.STATE_ACTIVE:
                item = menu.findItem(R.id.action_restore_pill_reminder);
                item.setVisible(false);
                break;
            case PillReminder.STATE_ARCHIVE:
                item = menu.findItem(R.id.action_archive_pill_reminder);
                item.setVisible(false);
                break;
            case PillReminder.STATE_DELETED:
                item = menu.findItem(R.id.action_delete_pill_reminder);
                item.setVisible(false);
                break;
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_save_pill_reminder:
                if (checkPill()) {
                    savePill();
                    fragment = PillsReminderFragment.newInstance(mPillPillReminder.getPillReminder().getmStatus());
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack("pills2")
                            .commit();
                }
                break;
            case R.id.action_archive_pill_reminder:
                db.updatePillReminderState(pillReminderId,PillReminder.STATE_ARCHIVE);
                db.closeDB();
                fragment = PillsReminderFragment.newInstance(PillReminder.STATE_ARCHIVE);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack("pills2")
                        .commit();
                break;
            case R.id.action_restore_pill_reminder:
                db.updatePillReminderState(pillReminderId,PillReminder.STATE_ACTIVE);
                db.closeDB();
                fragment = PillsReminderFragment.newInstance(PillReminder.STATE_ACTIVE);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack("pills2")
                        .commit();
                break;
            case R.id.action_delete_pill_reminder:
                db.updatePillReminderState(pillReminderId,PillReminder.STATE_DELETED);
                db.closeDB();
                fragment = PillsReminderFragment.newInstance(PillReminder.STATE_DELETED);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack("pills2")
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkPill() {
        return !etEveryHours.getText().toString().trim().equalsIgnoreCase("") || !etDescription.getText().toString().trim().equalsIgnoreCase("");
    }

    private void savePill() {
        mPillPillReminder.getPillReminder().setMhourStart(tvHourStart.getText().toString());
        mPillPillReminder.getPillReminder().setmDateStart(tvDateStart.getText().toString());
        mPillPillReminder.getPillReminder().setmDateFinish(tvDateFinish.getText().toString());
        mPillPillReminder.getPillReminder().setMhourStart(tvHourStart.getText().toString());
        mPillPillReminder.getPillReminder().setmEveryHours(Integer.parseInt(etEveryHours.getText().toString()));
        mPillPillReminder.getPillReminder().setmDescription(etDescription.getText().toString());

        db.updatePillReminder(mPillPillReminder.getPillReminder());

        if(mPillPillReminder.getPillReminder().getmStatus() == PillReminder.STATE_ACTIVE){
            createAlarm(mPillPillReminder.getPillReminder().getmReminderId());
        }
    }
    private void createAlarm(int pillReminderId) {
        AlarmScheduler alarm = new AlarmScheduler();

        Pill_PillReminder pillpillReminder = db.getPillReminderWithPill(pillReminderId);

        int reminderId = pillReminderId;
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int everyHour = pillpillReminder.getPillReminder().getmEveryHours();

        String date = pillpillReminder.getPillReminder().getmDateStart();

        year = Integer.parseInt(date.substring(6,10));
        month = Integer.parseInt(date.substring(3,5)) - 1;
        day = Integer.parseInt(date.substring(0,2));

        String time = pillpillReminder.getPillReminder().getMhourStart();

        hour = Integer.parseInt(time.substring(0,2));
        minute = Integer.parseInt(time.substring(3,5));
        alarm.scheduleAlarm(getActivity(),reminderId,year,month,day,hour,minute,everyHour);
    }

    private class DialogSelect extends DialogDate {

        int code;

        private DialogSelect(int code) {
            this.code = code;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            changeDate(year,month,day,code);
        }
    }

    private void changeDate(int year, int month, int day,int code) {
        String mon;
        if (month <= 10){
            mon = "0" + String.valueOf(month + 1);
        } else {
            mon = String.valueOf(month + 1);
        }

        String da;
        if (day < 10){
            da = "0" + String.valueOf(day);
        } else {
            da = String.valueOf(day);
        }

        String date = da + "/" + mon + "/" + String.valueOf(year);
        SpannableString spanString = new SpannableString(date);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
        switch (code) {
            case TypeSelectFragment.CODE_START:
                tvDateStart.setText(spanString);
                break;
            case TypeSelectFragment.CODE_FINISH:
                tvDateFinish.setText(spanString);
                break;
        }
    }

    private class DialogTimeSelect extends DialogHour {
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
    }
}
