package com.plaglabs.pillreminder.app;

import android.os.Bundle;

public class MainActivity extends AbstractNavDrawerActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ( savedInstanceState == null ) {
            getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentOne()).commit();
        }
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create( 100, "Demos"),
                NavMenuItem.create(101,getResources().getString(R.string.navdrawer_item_activepills), R.drawable.action_search, false, this),
                NavMenuItem.create(102,getResources().getString(R.string.navdrawer_item_archived),R.drawable.action_search, true, this),
                NavMenuItem.create(103,getResources().getString(R.string.navdrawer_item_deleted),R.drawable.action_search, false, this),
                NavMenuSection.create(200, "General"),
                NavMenuItem.create(201, getResources().getString(R.string.navdrawer_item_feedback), R.drawable.action_search, false, this),
                NavMenuItem.create(202, getResources().getString(R.string.navdrawer_item_help),R.drawable.action_search , false, this)};

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
        /*switch (id) {
            case 101:
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentThree()).commit();
                break;
            case 102:
                getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentThree()).commit();
                break;
        }*/
    }
}