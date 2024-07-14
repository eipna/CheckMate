package com.serbi.checkmate.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import com.serbi.checkmate.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private ListPreference listTheme;

    private String prefsTheme;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        initializePreferences();
        initializePreferencesValues();

        listTheme.setOnPreferenceChangeListener((preference, newValue) -> {
            String selectedTheme = (String) newValue;
            listTheme.setSummary(selectedTheme);

            switch (selectedTheme) {
                case "System":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString("THEME", "System");
                    break;
                case "Light":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString("THEME", "Light");
                    break;
                case "Dark":
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sharedPreferencesEditor = sharedPreferences.edit();
                    sharedPreferencesEditor.putString("THEME", "Dark");
                    break;
            }
            sharedPreferencesEditor.apply();
            return true;
        });
    }

    private void initializePreferences() {
        sharedPreferences = getActivity().getSharedPreferences("MINDCHECK", Context.MODE_PRIVATE);
        prefsTheme = sharedPreferences.getString("THEME", "System");

        listTheme = findPreference("list_theme");
    }

    private void initializePreferencesValues() {
        listTheme.setSummary(prefsTheme);
        listTheme.setValue(prefsTheme);
    }
}