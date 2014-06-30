package com.plaglabs.pillreminder.app.Pills;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plaglabs.pillreminder.app.R;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

public class PillCard extends Card {
    protected TextView mTitle;
    protected TextView mSecondaryTitle;
    protected int resourceIdThumbnail;

    protected int card_pill_id;

    protected String title;
    protected String secondaryTitle;

    public PillCard(Context context) {
        this(context, R.layout.pill_card);
    }

    public PillCard(Context context, int innerLayout) {
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

        setSwipeable(false);

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

    public int getCard_pill_id() {
        return card_pill_id;
    }

    public void setCard_pill_id(int card_pill_id) {
        this.card_pill_id = card_pill_id;
    }
}

