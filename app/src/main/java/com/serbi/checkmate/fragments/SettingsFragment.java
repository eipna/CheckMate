package com.serbi.checkmate.fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.serbi.checkmate.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}