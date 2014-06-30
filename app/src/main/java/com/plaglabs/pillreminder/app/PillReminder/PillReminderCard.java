package com.plaglabs.pillreminder.app.PillReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.Alarm.AlarmReciever;
import com.plaglabs.pillreminder.app.R;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.PillReminder;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by plagueis on 10/05/14.
 */
public class PillReminderCard extends Card {
    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected TextView mStartEvery;
    protected int resourceIdThumbnail;
    protected int card_pillReminder_id;

    public int getCard_pillReminder_status() {
        return card_pillReminder_status;
    }

    public void setCard_pillReminder_status(int card_pillReminder_status) {
        this.card_pillReminder_status = card_pillReminder_status;
    }

    protected int card_pillReminder_status;

    protected String title;
    protected String secondaryTitle;
    protected String startEvery;
    protected String dayStartFinish;

    public String getDayStartFinish() {
        return dayStartFinish;
    }

    public void setDayStartFinish(String dayStartFinish) {
        this.dayStartFinish = dayStartFinish;
    }

    public String getStartEvery() {
        return startEvery;
    }

    public void setStartEvery(String startEvery) {
        this.startEvery = startEvery;
    }

    public PillReminderCard(Context context) {
        this(context, R.layout.pill_reminder_card);
    }

    public PillReminderCard(Context context, int innerLayout) {
        super(context, innerLayout);
    }

    public void init() {

        //Add thumbnail
        CardThumbnail cardThumbnail = new CardThumbnail(mContext);

        if (resourceIdThumbnail == 0)
            cardThumbnail.setDrawableResource(R.drawable.ic_launcher);
        else {
            cardThumbnail.setDrawableResource(resourceIdThumbnail);
        }

        addCardThumbnail(cardThumbnail);

        setSwipeable(true);

        setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                PillReminderDBHelper db = new PillReminderDBHelper(getContext());
                switch (card_pillReminder_status){
                    case PillReminder.STATE_ACTIVE:
                        db.updatePillReminderState(card_pillReminder_id, PillReminder.STATE_ARCHIVE);
                        unableAlarm(card_pillReminder_id);
                        Toast.makeText(getContext(),mContext.getString(R.string.reminderArchived),Toast.LENGTH_SHORT).show();
                        break;
                    case PillReminder.STATE_ARCHIVE:
                        db.updatePillReminderState(card_pillReminder_id, PillReminder.STATE_DELETED);
                        Toast.makeText(getContext(),mContext.getString(R.string.reminderDeleted),Toast.LENGTH_SHORT).show();
                        break;
                    case PillReminder.STATE_DELETED:
                        db.deletePillReminder(card_pillReminder_id);
                        Toast.makeText(getContext(),mContext.getString(R.string.reminderRemoved),Toast.LENGTH_SHORT).show();
                        break;
                }
                db.closeDB();
            }
        });

    }

    private void unableAlarm(int card_pillReminder_id) {
        Intent intentAlarm = new Intent(mContext, AlarmReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,card_pillReminder_id, intentAlarm, 0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_title);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.card_secondaryTitle);
        mStartEvery = (TextView) parent.findViewById(R.id.startEvery);
        if (mTitle != null)
            mTitle.setText(title);

        if (mSecondaryTitle != null)
            mSecondaryTitle.setText(secondaryTitle);

        if(mStartEvery !=null){
            mStartEvery.setText(startEvery);
        }

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public int getResourceIdThumbnail() {
        return resourceIdThumbnail;
    }

    public void setResourceIdThumbnail(int resourceIdThumbnail) {
        this.resourceIdThumbnail = resourceIdThumbnail;
    }

    public int getCard_pillReminder_id() {
        return card_pillReminder_id;
    }

    public void setCard_pillReminder_id(int card_pillReminder_id) {
        this.card_pillReminder_id = card_pillReminder_id;
    }
}

