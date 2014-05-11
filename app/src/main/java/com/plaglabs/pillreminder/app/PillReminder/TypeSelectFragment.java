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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.R;
import com.plaglabs.pillreminder.app.Utils.DialogConfirmation;
import com.plaglabs.pillreminder.app.Utils.DialogDate;

/**
 * Created by plagueis on 11/05/14.
 */
public class TypeSelectFragment extends Fragment {
    public final static int CODE_START = 1;
    public final static int CODE_FINISH = 2;
    private int pill_id;
    private boolean tvDateStartHasDate=false,tvDateFinishHasDate=false;

    Button btnNext,btnCancel;
    public TextView tvDateStart,tvDateFinish;
    RadioButton rbHours, rbDays;

    public TypeSelectFragment() {
    }

    public TypeSelectFragment(int pill_id) {
        this.pill_id = pill_id;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_type_select, container,false);

        rbHours = (RadioButton) view.findViewById(R.id.rbHours);
        rbHours.setChecked(true);
        rbDays = (RadioButton) view.findViewById(R.id.rbDays);

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
                checkNext();

                if (checkNext()){
                    Fragment fragment;
                    if(rbHours.isChecked()){
                        fragment = new EveryHoursFragment();
                    } else {
                        fragment = new DaysMealsFragment();
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

        if(tvDateStartHasDate && tvDateFinishHasDate){
            next = true;
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
        String date = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
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
