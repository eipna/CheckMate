package com.serbi.checkmate.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.serbi.checkmate.R;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;

    private ListPreference listTheme;
    private Preference listLibraries;

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

        listLibraries.setOnPreferenceClickListener(preference -> {
            showLibrariesListDialog();
            return true;
        });
    }

    // Shows the dialog of list of libraries
    private void showLibrariesListDialog() {
        String[] librariesItems = getResources().getStringArray(R.array.list_libraries);
        View listViewContainer = getLayoutInflater().inflate(R.layout.dialog_libraries_list, null);
        ListView librariesListView = listViewContainer.findViewById(R.id.listview_libraries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, librariesItems);
        librariesListView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Libraries").
                setView(listViewContainer)
                .setPositiveButton("Cancel", null);

        librariesListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedLibrary = librariesItems[position];
            Intent browserIntent = null;

            switch (selectedLibrary) {
                case "PrettyTime":
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.webite_pretty_time)));
            }
            startActivity(browserIntent);
        });

        builder.create().show();
    }

    private void initializePreferences() {
        sharedPreferences = getActivity().getSharedPreferences("MINDCHECK", Context.MODE_PRIVATE);
        prefsTheme = sharedPreferences.getString("THEME", "System");

        listTheme = findPreference("list_theme");
        listLibraries = findPreference("list_libraries");
    }

    private void initializePreferencesValues() {
        listTheme.setSummary(prefsTheme);
        listTheme.setValue(prefsTheme);
    }
}