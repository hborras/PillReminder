package com.plaglabs.pillreminder.app.PillReminder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                        break;
                    case PillReminder.STATE_ARCHIVE:
                        db.updatePillReminderState(card_pillReminder_id, PillReminder.STATE_DELETED);
                        break;
                    case PillReminder.STATE_DELETED:
                        db.deletePillReminder(card_pillReminder_id);
                        break;
                }
                db.closeDB();
            }
        });

    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_title);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.card_secondaryTitle);

        if (mTitle != null)
            mTitle.setText(title);

        if (mSecondaryTitle != null)
            mSecondaryTitle.setText(secondaryTitle);
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

