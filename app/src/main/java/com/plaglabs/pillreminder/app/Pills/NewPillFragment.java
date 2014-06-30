package com.plaglabs.pillreminder.app.Pills;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.plaglabs.pillreminder.app.Utils.DialogConfirmation;
import com.plaglabs.pillreminder.app.Utils.Image;
import com.plaglabs.pillreminder.app.Utils.ImagesPageAdapter;
import com.plaglabs.pillreminder.app.R;

import java.util.ArrayList;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.Pill;

/**
 * Created by plagueis on 10/05/14.
 */
public class NewPillFragment extends Fragment {

    PillReminderDBHelper db;
    ViewPager viewPager;
    ArrayList<Image> mImages;
    EditText etName;
    Pill pill;

    public NewPillFragment() {
    }

    public NewPillFragment(Pill pill) {
        this.pill = pill;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(pill!=null){
            inflater.inflate(R.menu.pills_menu_delete, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogConfirmation dialogConfirmation =     null;
        dialogConfirmation = DialogConfirmation.newInstance(R.string.dialog_remove_pill_title,
                R.string.dialog_remove_pill_message, DialogConfirmation.DELETE_PILL,pill.getmId(),0);
        dialogConfirmation.show(getFragmentManager(), "dialog");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_new_pill, container,
                false);
        viewPager = (ViewPager) view.findViewById(R.id.pillPager);

        mImages = new ArrayList<Image>();

        Image image = new Image(1,R.drawable.aspirine_blue);
        mImages.add(image);
        image = new Image(2,R.drawable.aspirine_green);
        mImages.add(image);
        image = new Image(3,R.drawable.aspirine_grey);
        mImages.add(image);
        image = new Image(4,R.drawable.aspirine_orange);
        mImages.add(image);
        image = new Image(5,R.drawable.aspirine_purple);
        mImages.add(image);
        image = new Image(6,R.drawable.aspirine_red);
        mImages.add(image);
        image = new Image(7,R.drawable.pill_blue);
        mImages.add(image);
        image = new Image(8,R.drawable.pill_green);
        mImages.add(image);
        image = new Image(9,R.drawable.pill_grey);
        mImages.add(image);
        image = new Image(10,R.drawable.pill_orange);
        mImages.add(image);
        image = new Image(11,R.drawable.pill_purple);
        mImages.add(image);
        image = new Image(12,R.drawable.pill_red);
        mImages.add(image);
        image = new Image(13,R.drawable.container_blue);
        mImages.add(image);
        image = new Image(14,R.drawable.container_green);
        mImages.add(image);
        image = new Image(15,R.drawable.container_grey);
        mImages.add(image);
        image = new Image(16,R.drawable.container_orange);
        mImages.add(image);
        image = new Image(17,R.drawable.container_purple);
        mImages.add(image);
        image = new Image(18,R.drawable.container_red);
        mImages.add(image);

        ImagesPageAdapter adapter = new ImagesPageAdapter(getActivity(),mImages);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80, 0, 80, 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Button btnSave,btnCancel;

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pag = viewPager.getCurrentItem();
                Image image = mImages.get(pag);
                String text = etName.getText().toString();
                if (text.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(),getResources().getString(R.string.new_pill_error_text),Toast.LENGTH_SHORT).show();
                } else {
                    if (pill != null){
                        pill.setmName(text);
                        pill.setmImage(image.getrID());
                        updatePill();
                    } else {
                        addPill(text, image.getrID());
                    }
                    returnBack();
                }
            }
        });

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                returnBack();
            }
        });

        etName = (EditText) view.findViewById(R.id.etName);

        if (pill!=null){
            etName.setText(pill.getmName());

            boolean stop = false;
            int i = 0;
            while(stop == false && i < mImages.size()){
                Image mImage = mImages.get(i);
                if (mImage.getrID() == pill.getmImage()){
                    stop = true;
                    viewPager.setCurrentItem(i,true);
                }
                i++;
            }
        }

        return view;
    }

    private void addPill(String name, int rId) {
        db = new PillReminderDBHelper(getActivity());

        Pill pill = new Pill(name,rId);

        long pill_id = db.createPill(pill);

        db.closeDB();
    }

    private void updatePill(){
        db = new PillReminderDBHelper(getActivity());

        db.updatePill(pill);

        db.closeDB();
    }

    private void returnBack(){
        getActivity().getFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new PillsFragment())
                .addToBackStack("newPill")
                .commit();
    }
}
