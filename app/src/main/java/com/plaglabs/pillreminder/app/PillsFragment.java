package com.plaglabs.pillreminder.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.Pill;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.listener.UndoBarController;

/**
 * Created by plagueis on 10/05/14.
 */
public class PillsFragment extends Fragment {

    List<Pill> mPills;
    PillReminderDBHelper db;
    CardListView mPillsList;
    CardArrayAdapter mCardArrayAdapter;
    private UndoBarController mUndoBarController;

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
        for (Pill pill : mPills){
            CardItem card = new CardItem(getActivity());
            //Create a CardHeader
            //CardHeader header = new CardHeader(getActivity());
            //header.setTitle(pill.getmName());
            //Add Header to card
            //card.addCardHeader(header);

            card.setResourceIdThumbnail(pill.getmImage());
            //card.setSecondaryTitle(noticia.getFecha());
            card.setTitle(pill.getmName());
            card.setCard_pill_id(pill.getmId());


            card.init();
            cards.add(card);
        }
        mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);

        mCardArrayAdapter.setEnableUndo(true);

        if (mPillsList!=null){
            mPillsList.setAdapter(mCardArrayAdapter);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pills, container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pills_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(getActivity(), "View: " + item.toString(), Toast.LENGTH_SHORT).show();

        getActivity().getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new NewPillFragment())
                .addToBackStack("newPill")
                .commit();

        return super.onOptionsItemSelected(item);
    }
}
