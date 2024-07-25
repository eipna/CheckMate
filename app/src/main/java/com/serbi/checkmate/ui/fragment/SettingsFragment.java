package com.serbi.checkmate.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.serbi.checkmate.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private ListPreference listTheme;
    private Preference listLibraries, staticAppVersion;

    private String prefsTheme;

    // For Easter Egg :D
    private int easterEggCounter;

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

        listLibraries.setOnPreferenceClickListener(preference -> {
            showLibrariesListDialog();
            return true;
        });

        staticAppVersion.setOnPreferenceClickListener(preference -> {
            easterEggCounter++;
            if (easterEggCounter == 5) {
                Toast.makeText(requireContext(), getResources().getString(R.string.app_easter_egg), Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // Shows the dialog of list of libraries
    private void showLibrariesListDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireActivity());
        dialog.setTitle("Libraries")
                .setItems(R.array.list_libraries, (dialog1, which) -> {
                    Intent browserIntent = null;
                    if (which == 0) {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse
                                (getResources().getString(R.string.website_pretty_time)));
                    }
                    if (which == 1) {
                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse
                                (getResources().getString(R.string.website_work_manager)));
                    }
                    startActivity(browserIntent);
                }).setPositiveButton("Go Back", null);
        dialog.create().show();
    }

    private void initializePreferences() {
        sharedPreferences = getActivity().getSharedPreferences("MINDCHECK", Context.MODE_PRIVATE);
        prefsTheme = sharedPreferences.getString("THEME", "System");

        listTheme = findPreference("list_theme");
        listLibraries = findPreference("list_libraries");
        staticAppVersion = findPreference("static_app_version");
    }

    private void initializePreferencesValues() {
        listTheme.setSummary(prefsTheme);
        listTheme.setValue(prefsTheme);
    }
}