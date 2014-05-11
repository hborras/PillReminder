package com.plaglabs.pillreminder.app.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by plagueis on 11/05/14.
 */
public class DialogDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static DialogDate newInstance(int title, int message) {
        DialogDate frag = new DialogDate();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("message",message);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
    }
}
