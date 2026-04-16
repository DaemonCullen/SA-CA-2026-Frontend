package com.example.sa_ca_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GoalsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnLoseWeight).setOnClickListener(v ->
                navigateToSplash(
                        "Lose Weight",
                        "We'll put together a calorie-conscious meal plan to help you shed weight steadily and sustainably.",
                        "Fat Loss Plan",
                        "calories",
                        -1
                )
        );

        view.findViewById(R.id.btnGainMuscle).setOnClickListener(v ->
                navigateToSplash(
                        "Gain Muscle",
                        "High-protein meals selected to fuel your training and support muscle growth every day of the week.",
                        "Muscle Building Plan",
                        "protein",
                        -1
                )
        );

        view.findViewById(R.id.btnLearnCooking).setOnClickListener(v ->
                navigateToSplash(
                        "Learn Cooking",
                        "Loading Plans For you to cook easily while you are busy",
                        "Health Plan",
                        "none",
                        -1
                )
        );
    }

    private void navigateToSplash(String goalTitle, String description,
                                   String planName, String filterType, double filterValue) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout,
                        ChosenPlanSplashFragment.newInstance(goalTitle, description, planName, filterType, filterValue))
                .addToBackStack(null)
                .commit();
    }
}
