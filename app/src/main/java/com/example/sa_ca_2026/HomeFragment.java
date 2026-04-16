package com.example.sa_ca_2026;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView cardMeals = view.findViewById(R.id.cardMeals);
        CardView cardPlans = view.findViewById(R.id.cardPlans);
        CardView cardSettings = view.findViewById(R.id.cardSettings);

        BottomNavigationView navBar = requireActivity().findViewById(R.id.bottomNavigationView);

        cardMeals.setOnClickListener(v -> {
            navBar.setSelectedItemId(R.id.meals);
        });

        cardPlans.setOnClickListener(v -> {
            navBar.setSelectedItemId(R.id.action_plans);
        });

        cardSettings.setOnClickListener(v -> {
            navBar.setSelectedItemId(R.id.settings);
        });
    }
}
