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
import android.widget.Toast;

import com.plaglabs.pillreminder.app.Pills.NewPillFragment;
import com.plaglabs.pillreminder.app.Pills.PillCard;
import com.plaglabs.pillreminder.app.R;
import com.plaglabs.pillreminder.app.Utils.DialogConfirmation;

import java.util.ArrayList;
import java.util.List;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.Pill;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.UndoBarController;

/**
 * Created by plagueis on 10/05/14.
 */
public class PillSelectFragment extends Fragment {

    List<Pill> mPills;
    PillReminderDBHelper db;
    CardListView mPillsList;
    CardArrayAdapter mCardArrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new PillReminderDBHelper(getActivity());
        mPills = db.getAllPills();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPillsList = (CardListView) getActivity().findViewById(R.id.pillsList);
        initCards();
    }

    private void initCards() {

        ArrayList<Card> cards = new ArrayList<Card>(mPills.size());
        for (final Pill pill : mPills){
            PillCard card = new PillCard(getActivity());
            card.setResourceIdThumbnail(pill.getmImage());
            card.setTitle(pill.getmName());
            card.setCard_pill_id(pill.getmId());
            card.init();

            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, new TypeSelectFragment(pill.getmId()))
                            .addToBackStack("Pills3")
                            .commit();
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
        return inflater.inflate(R.layout.fragment_select_pills, container,false);
    }
}
