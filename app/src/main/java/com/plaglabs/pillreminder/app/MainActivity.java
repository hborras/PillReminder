package com.plaglabs.pillreminder.app;

import android.content.Intent;
import android.os.Bundle;

import com.plaglabs.pillreminder.app.PillReminder.PillSelectFragment;
import com.plaglabs.pillreminder.app.PillReminder.PillsReminderArchiveFragment;
import com.plaglabs.pillreminder.app.PillReminder.PillsReminderDeleteFragment;
import com.plaglabs.pillreminder.app.PillReminder.PillsReminderFragment;
import com.plaglabs.pillreminder.app.Pills.PillsFragment;

import SQLite.Model.PillReminder;

public class MainActivity extends AbstractNavDrawerActivity {
    public static final int DEBUG = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( savedInstanceState == null ) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, new PillsReminderFragment(PillReminder.STATE_ACTIVE))
                    .addToBackStack("Pills1")
                    .commit();
        }
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuItem.create(101,getResources().getString(R.string.navdrawer_item_nextpills), R.drawable.action_search, true, this),
                NavMenuItem.create(102,getResources().getString(R.string.navdrawer_item_activepills), R.drawable.action_search, true, this),
                NavMenuItem.create(103,getResources().getString(R.string.navdrawer_item_pills), R.drawable.pill_red, true, this),
                NavMenuItem.create(104,getResources().getString(R.string.navdrawer_item_archived),R.drawable.action_search, true, this),
                NavMenuItem.create(105,getResources().getString(R.string.navdrawer_item_deleted),R.drawable.action_search, true, this),
                NavMenuSection.create(200, getResources().getString(R.string.others)),
                NavMenuItem.create(201, getResources().getString(R.string.navdrawer_item_feedback), R.drawable.action_search, true, this),
                NavMenuItem.create(202, getResources().getString(R.string.navdrawer_item_help),R.drawable.ic_action_help , true, this)};

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.activity_main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PillSelectFragment())
                        .addToBackStack("pills2")
                        .commit();
                break;
            case 102:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PillsReminderFragment(PillReminder.STATE_ACTIVE))
                        .addToBackStack("pills2")
                        .commit();
                break;
            case 103:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PillsFragment())
                        .addToBackStack("pills3")
                        .commit();
                break;

            case 104:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PillsReminderFragment(PillReminder.STATE_ARCHIVE))
                        .addToBackStack("pills2")
                        .commit();
                break;

            case 105:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new PillsReminderFragment(PillReminder.STATE_DELETED))
                        .addToBackStack("pills2")
                        .commit();
                break;
            case 201:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"feedback@pillreminder.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                startActivity(intent);
                break;

        }
    }
}