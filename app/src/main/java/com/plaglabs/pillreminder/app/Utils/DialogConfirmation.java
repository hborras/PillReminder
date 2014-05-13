package com.plaglabs.pillreminder.app.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.plaglabs.pillreminder.app.PillReminder.PillsReminderFragment;
import com.plaglabs.pillreminder.app.Pills.PillsFragment;
import com.plaglabs.pillreminder.app.R;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.Pill;

/**
 * Created by plagueis on 10/05/14.
 */
public class DialogConfirmation extends DialogFragment {

    public static final int DELETE_PILL = 1;
    public static final int DELETE_PILL_REMINDER = 2;

    PillReminderDBHelper db;

    public static DialogConfirmation newInstance(int title, int message, int code,int id) {
        DialogConfirmation frag = new DialogConfirmation();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("message",message);
        args.putInt("code",code);
        args.putInt("id",id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int message = getArguments().getInt("message");
        int title = getArguments().getInt("title");
        final int code = getArguments().getInt("code");
        final int id = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment fragment = null;
                        switch (code) {
                            case DELETE_PILL:
                                db = new PillReminderDBHelper(getActivity());
                                    db.deletePill(id,true);
                                db.closeDB();
                                fragment = new PillsFragment();
                                break;
                            case DELETE_PILL_REMINDER:
                                db = new PillReminderDBHelper(getActivity());
                                db.deletePillReminder(id);
                                db.closeDB();
                                fragment = new PillsReminderFragment();
                                break;

                        }
                        getActivity().getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .addToBackStack("newPill")
                                .commit();
                    }
                })
                .setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}

