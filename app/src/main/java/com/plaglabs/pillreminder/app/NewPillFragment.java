package com.plaglabs.pillreminder.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SQLite.Database.PillReminderDBHelper;
import SQLite.Model.Pill;

/**
 * Created by plagueis on 10/05/14.
 */
public class NewPillFragment extends Fragment{

    PillReminderDBHelper db;
    ViewPager viewPager;
    ArrayList<Image> mImages;
    EditText etName;

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
                //Toast.makeText(getActivity(),"Page: " + position,Toast.LENGTH_SHORT).show();
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


                addPill(etName.getText().toString(),image.getrID());
            }
        });

        etName = (EditText) view.findViewById(R.id.etName);

        return view;
    }

    private void addPill(String name, int rId) {
        db = new PillReminderDBHelper(getActivity());

        Pill pill = new Pill(name,rId);

        long pill_id = db.createPill(pill);
        Toast.makeText(getActivity(),"Created Pill with ID: " + pill_id,Toast.LENGTH_SHORT).show();
        db.closeDB();
    }


}
