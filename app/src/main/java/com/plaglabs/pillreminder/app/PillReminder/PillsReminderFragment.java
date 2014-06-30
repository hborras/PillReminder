package com.plaglabs.pillreminder.app.PillReminder;

import android.app.Fragment;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.plaglabs.pillreminder.app.Alarm.AlarmScheduler;
import com.plaglabs.pillreminder.app.R;
import com.plaglabs.pillreminder.app.Utils.DialogConfirmation;

import java.util.ArrayList;
import java.util.List;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.PillReminder;
import SQLite.Model.Pill_PillReminder;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;

public class PillsReminderFragment extends Fragment {

    List<Pill_PillReminder> mPills;
    PillReminderDBHelper db;
    CardListView mPillsList;
    CardArrayAdapter mCardArrayAdapter;
    int position = -1;
    ActionMode mActionMode;
    private int status;

    public static PillsReminderFragment newInstance(int status) {
        PillsReminderFragment frag = new PillsReminderFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt("status");
        setHasOptionsMenu(true);
        db = new PillReminderDBHelper(getActivity());
        mPills = db.getAllPillRemindersWithPill(status);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPillsList = (CardListView) getActivity().findViewById(R.id.pillsList);
        initCards();
    }

    private void initCards() {

        ArrayList<Card> cards = new ArrayList<Card>(mPills.size());
        for (final Pill_PillReminder pillPillReminder : mPills){
            PillReminderCard card = new PillReminderCard(getActivity());

            //Create a CardHeader
            CardHeader header = new CardHeader(getActivity());
            header.setTitle(pillPillReminder.getPill().getmName());
            header.setButtonExpandVisible(false);
            //Add Header to card
            card.addCardHeader(header);
            card.setCard_pillReminder_id(pillPillReminder.getPillReminder().getmReminderId());
            card.setCard_pillReminder_status(pillPillReminder.getPillReminder().getmStatus());

            card.setResourceIdThumbnail(pillPillReminder.getPill().getmImage());
            card.setTitle(pillPillReminder.getPillReminder().getmDescription());
            card.setSecondaryTitle(getResources().getString(R.string.dates)+ pillPillReminder.getPillReminder().getmDateStart()
            + " - " + pillPillReminder.getPillReminder().getmDateFinish());
            card.setStartEvery(getResources().getQuantityString(R.plurals.hours,pillPillReminder.getPillReminder().getmEveryHours(),pillPillReminder.getPillReminder().getmEveryHours()));
            card.init();

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    if(mActionMode!=null){
                        mActionMode.finish();
                    } else {
                        Fragment fragment = PillReminderFragment.newInstance(pillPillReminder.getPillReminder().getmReminderId());
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.content_frame, fragment)
                                .addToBackStack("pills2")
                                .commit();
                    }
                }
            });

            card.setOnLongClickListener(new Card.OnLongCardClickListener(){

                @Override
                public boolean onLongClick(Card card, View view) {
                    if (mActionMode != null) {
                        mActionMode.finish();
                        return false;
                    } else {
                        // Start the CAB using the ActionMode.Callback defined above
                        mActionMode = getActivity().startActionMode(mActionModeCallback);

                        position = mCardArrayAdapter.getPosition(card);
                        mPillsList.setItemChecked(position,true);
                    }
                    return true;
                }
            });

            cards.add(card);
        }
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        if (mPillsList!=null){
            mPillsList.setAdapter(mCardArrayAdapter);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pills_reminders, container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*if (position != -1){
            inflater.inflate(R.menu.pills_reminder_menu_delete, menu);
        } else {*/
            inflater.inflate(R.menu.pills_reminder_menu, menu);
       // }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_pill_reminder:
                getActivity().getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, new PillSelectFragment())
                    .addToBackStack("newPill")
                    .commit();
                break;
            case R.id.action_view_pill_reminder:

                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            switch (status){
                case PillReminder.STATE_ACTIVE:
                    inflater.inflate(R.menu.pills_reminder_menu_view_archive_delete, menu);
                    break;
                case PillReminder.STATE_ARCHIVE:
                    inflater.inflate(R.menu.pills_reminder_menu_view_restore_delete, menu);
                    break;
                case PillReminder.STATE_DELETED:
                    inflater.inflate(R.menu.pills_reminder_menu_view_archive_restore_delete,menu);
                    menu.findItem(R.id.action_delete_pill_reminder).setVisible(false);
                    break;
            }

            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            boolean flag = false;
            switch (item.getItemId()) {
                case R.id.action_delete_pill_reminder:
                    deletePillReminder();
                    mode.finish(); // Action picked, so close the CAB
                    flag = true;
                    break;
                case R.id.action_archive_pill_reminder:
                    archivePillReminder();
                    mode.finish();
                    flag=true;
                    break;
                case R.id.action_view_pill_reminder:
                    viewPillReminder();
                    mode.finish();
                    flag=true;
                    break;
                case R.id.action_restore_pill_reminder:
                    restorePillReminder();
                    mode.finish();
                    flag=true;
                    break;
                default:
                    mode.finish();
                flag = false;
            }
            return flag;
        }

        private void restorePillReminder() {
            PillReminderCard pillReminderCard = (PillReminderCard) mPillsList.getItemAtPosition(position);
            db.updatePillReminderState(pillReminderCard.getCard_pillReminder_id(),PillReminder.STATE_ACTIVE);
            db.closeDB();
            Fragment fragment = PillsReminderFragment.newInstance(PillReminder.STATE_ARCHIVE);
            createAlarm(pillReminderCard.getCard_pillReminder_id());
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack("pills2")
                    .commit();
        }

        private void viewPillReminder() {
            PillReminderCard pillReminderCard = (PillReminderCard) mPillsList.getItemAtPosition(position);
            Fragment fragment = PillReminderFragment.newInstance(pillReminderCard.getCard_pillReminder_id());
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack("pills2")
                    .commit();
        }


        private void archivePillReminder() {
            PillReminderCard pillReminderCard = (PillReminderCard) mPillsList.getItemAtPosition(position);
            db.updatePillReminderState(pillReminderCard.getCard_pillReminder_id(),PillReminder.STATE_ARCHIVE);
            db.closeDB();
            Fragment fragment = PillsReminderFragment.newInstance(PillReminder.STATE_ARCHIVE);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack("pills2")
                    .commit();
        }

        private void deletePillReminder() {
            PillReminderCard pillReminderCard = (PillReminderCard) mPillsList.getItemAtPosition(position);
            DialogConfirmation dialogConfirmation = null;
            dialogConfirmation = DialogConfirmation.newInstance(R.string.dialog_remove_pill_reminder_title,
                    R.string.dialog_remove_pill_reminder_message, DialogConfirmation.DELETE_PILL_REMINDER,pillReminderCard.getCard_pillReminder_id(),status);
            dialogConfirmation.show(getFragmentManager(), "dialog");
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            mPillsList.setItemChecked(position,false);
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
    };
}
