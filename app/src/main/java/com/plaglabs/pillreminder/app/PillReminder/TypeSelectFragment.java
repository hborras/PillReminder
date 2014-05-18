package com.plaglabs.pillreminder.app.PillReminder;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.R;
import com.plaglabs.pillreminder.app.Utils.DialogConfirmation;
import com.plaglabs.pillreminder.app.Utils.DialogDate;

import java.util.Calendar;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.PillReminder;
import SQLite.Model.Pill_PillReminder;

/**
 * Created by plagueis on 11/05/14.
 */
public class TypeSelectFragment extends Fragment {
    public final static int CODE_START = 1;
    public final static int CODE_FINISH = 2;
    private PillReminder pillReminder;
    private boolean tvDateStartHasDate=false,tvDateFinishHasDate=false;

    Button btnNext,btnCancel;
    TextView tvDateStart,tvDateFinish;
    RadioButton rbHours, rbDays;
    EditText etDescription;

    /*public static TypeSelectFragment newInstance(int pillReminderId) {
        TypeSelectFragment frag = new TypeSelectFragment();
        Bundle args = new Bundle();
        args.putInt("pillReminderId", pillReminderId);
        frag.setArguments(args);
        return frag;
    }*/

    public TypeSelectFragment() {
    }

    public TypeSelectFragment(PillReminder pillReminder) {
        this.pillReminder = pillReminder;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pillId", pillReminder.getmPillId());
        outState.putInt("status",pillReminder.getmStatus());
        outState.putString("description",pillReminder.getmDescription());
        outState.putString("dateStart",pillReminder.getmDateStart());
        outState.putString("dateFinish",pillReminder.getmDateFinish());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState !=null){
            pillReminder = new PillReminder();
            pillReminder.setmPillId(savedInstanceState.getInt("pillId"));
            pillReminder.setmStatus(savedInstanceState.getInt("status"));
            pillReminder.setmDescription(savedInstanceState.getString("description"));
            pillReminder.setmDateStart(savedInstanceState.getString("dateStart"));
            pillReminder.setmDateFinish(savedInstanceState.getString("dateFinish"));
        }

        if( pillReminder != null) {
            if (!pillReminder.getmDescription().equalsIgnoreCase("")) {
                etDescription.setText(pillReminder.getmDescription());
            }

            if (!pillReminder.getmDateStart().equalsIgnoreCase("")) {
                SpannableString spanString = new SpannableString(pillReminder.getmDateStart());
                spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
                tvDateStart.setText(spanString);
                tvDateStartHasDate = true;
            }

            if (!pillReminder.getmDateFinish().equalsIgnoreCase("")) {
                SpannableString spanString2 = new SpannableString(pillReminder.getmDateFinish());
                spanString2.setSpan(new UnderlineSpan(), 0, spanString2.length(), 0);
                tvDateFinish.setText(spanString2);
                tvDateFinishHasDate = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_type_select, container,false);

        etDescription = (EditText) view.findViewById(R.id.etDescription);

        rbHours = (RadioButton) view.findViewById(R.id.rbHours);
        rbHours.setChecked(true);
        rbDays = (RadioButton) view.findViewById(R.id.rbDays);
        rbDays.setEnabled(false);

        tvDateStart = (TextView) view.findViewById(R.id.tvDateStart);

        SpannableString spanString = new SpannableString(tvDateStart.getText().toString());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);

        tvDateStart.setText(spanString);

        tvDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelect dialogDateSelect = new DialogSelect(CODE_START);
                dialogDateSelect.show(getFragmentManager(), "dialog");
            }
        });

        tvDateFinish = (TextView) view.findViewById(R.id.tvDateFinish);

        spanString = new SpannableString(tvDateFinish.getText().toString());
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);

        tvDateFinish.setText(spanString);

        tvDateFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelect dialogDateSelect = new DialogSelect(CODE_FINISH);
                dialogDateSelect.show(getFragmentManager(), "dialog");
            }
        });

        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNext()){

                    pillReminder.setmDescription(etDescription.getText().toString());
                    pillReminder.setmDateStart(tvDateStart.getText().toString());
                    pillReminder.setmDateFinish(tvDateFinish.getText().toString());

                    Fragment fragment;
                    if(rbHours.isChecked()){
                        fragment = new EveryHoursFragment(pillReminder);
                    } else {
                        fragment = new EveryHoursFragment(pillReminder);
                    }

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame,fragment)
                            .addToBackStack("new")
                            .commit();
                } else {
                    Toast.makeText(getActivity(), getResources().getString((R.string.type_select_date)),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PillSelectFragment())
                        .addToBackStack("Pills1")
                        .commit();
            }
        });


        return view;
    }

    private boolean checkNext() {
        boolean next = false;

        if(tvDateStartHasDate && tvDateFinishHasDate && !etDescription.getText().toString().trim().equalsIgnoreCase("")){

            int yearStart,yearFinish;
            int monthStart,monthFinish;
            int dayStart,dayFinish;

            String dateStart = tvDateStart.getText().toString();

            yearStart = Integer.parseInt(dateStart.substring(6,10));
            monthStart = Integer.parseInt(dateStart.substring(3,5)) - 1;
            dayStart = Integer.parseInt(dateStart.substring(0,2));

            String dateFinish = tvDateFinish.getText().toString();

            yearFinish = Integer.parseInt(dateFinish.substring(6,10));
            monthFinish = Integer.parseInt(dateFinish.substring(3,5)) - 1;
            dayFinish = Integer.parseInt(dateFinish.substring(0,2));

            Calendar cStart = Calendar.getInstance(), cFinish = Calendar.getInstance();

            cStart.set(Calendar.YEAR,yearStart);
            cStart.set(Calendar.MONTH,monthStart);
            cStart.set(Calendar.DAY_OF_MONTH,dayStart);
            cStart.set(Calendar.HOUR_OF_DAY,0);
            cStart.set(Calendar.MINUTE,0);
            cStart.set(Calendar.SECOND,0);

            cFinish.set(Calendar.YEAR,yearFinish);
            cFinish.set(Calendar.MONTH,monthFinish);
            cFinish.set(Calendar.DAY_OF_MONTH,dayFinish);
            cFinish.set(Calendar.HOUR_OF_DAY,0);
            cFinish.set(Calendar.MINUTE,0);
            cFinish.set(Calendar.SECOND,0);

            if(cFinish.getTimeInMillis()  >= cStart.getTimeInMillis()){
                next = true;
            }
        }


        return next;
    }

    private class DialogSelect extends DialogDate{

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
            case CODE_START:
                tvDateStart.setText(spanString);
                tvDateStartHasDate = true;
                break;
            case CODE_FINISH:
                tvDateFinish.setText(spanString);
                tvDateFinishHasDate = true;
                break;
        }


    }
}
