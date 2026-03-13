package com.example.sa_ca_2026;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AlertDialog;
import androidx.core.os.LocaleListCompat;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.SwitchCompat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void showLanguageDialog() {
        String[] languages = {"English", "Gaeilge", "Español"};
        String[] languageTags = {"en", "ga", "es"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Choose Language")
                .setItems(languages, (dialog, which) -> {
                    LocaleListCompat appLocale =
                            LocaleListCompat.forLanguageTags(languageTags[which]);
                    AppCompatDelegate.setApplicationLocales(appLocale);
                })
                .show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RelativeLayout darkModeRow = view.findViewById(R.id.dark_mode_item);
        SwitchCompat darkModeSwitch = view.findViewById(R.id.dark_mode_switch);

        RelativeLayout languageRow = view.findViewById(R.id.language_item);

        languageRow.setOnClickListener(v -> showLanguageDialog());

        int currentNightMode = getResources().getConfiguration().uiMode &
                android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        darkModeSwitch.setChecked(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES);

        darkModeRow.setOnClickListener(v -> {
            boolean isChecked = darkModeSwitch.isChecked();

            darkModeSwitch.jumpDrawablesToCurrentState();

            float start = isChecked ? 1f : 0f;
            float end = isChecked ? 0f : 1f;
            android.animation.ObjectAnimator animator = android.animation.ObjectAnimator.ofFloat(
                    darkModeSwitch, "thumbPosition", start, end
            );
            animator.setDuration(150);
            animator.start();

            darkModeSwitch.postDelayed(() -> {
                darkModeSwitch.setChecked(!isChecked);
                AppCompatDelegate.setDefaultNightMode(
                        !isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                );
            }, 150);
        });

        return view;
    }
}