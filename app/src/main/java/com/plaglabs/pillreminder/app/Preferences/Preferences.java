package com.plaglabs.pillreminder.app.Preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by plagueis on 14/06/14.
 */
public class Preferences extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
