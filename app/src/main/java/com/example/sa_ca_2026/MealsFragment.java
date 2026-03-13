package com.example.sa_ca_2026;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MealsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ListView listView;
    SearchView searchView;
    Spinner sortSpinner;
    Button filterButton;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> allMeals = new ArrayList<>(Arrays.asList(
            "Pizza", "Pasta", "Curry", "Burger", "Salad",
            "Ramen", "Roast", "Meatballs", "Tacos", "Meatloaf", "Stir Fry"
    ));

    ArrayList<String> displayedMeals = new ArrayList<>();

    public MealsFragment() {
    }

    public static MealsFragment newInstance(String param1, String param2) {
        MealsFragment fragment = new MealsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void updateMealList() {
        String query = searchView.getQuery().toString().toLowerCase().trim();
        String sortChoice = sortSpinner.getSelectedItem().toString();

        displayedMeals.clear();

        for (String meal : allMeals) {
            if (meal.toLowerCase().contains(query)) {
                displayedMeals.add(meal);
            }
        }

        Collections.sort(displayedMeals, String.CASE_INSENSITIVE_ORDER);

        if (sortChoice.equals("Z-A")) {
            Collections.reverse(displayedMeals);
        }

        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meals, container, false);

        listView = view.findViewById(R.id.mealsList);
        searchView = view.findViewById(R.id.searchView);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        filterButton = view.findViewById(R.id.filterButton);

        searchView.setQueryHint("Search meals");
        searchView.setIconifiedByDefault(false);

        displayedMeals.addAll(allMeals);

        arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.meal_list_item,
                R.id.mealName,
                displayedMeals
        );

        listView.setAdapter(arrayAdapter);

        String[] sortOptions = {"A-Z", "Z-A"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sortOptions
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        // Listens for search bar input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                updateMealList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateMealList();
                return false;
            }
        });

        // Listens for sort update
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateMealList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* This Does Not Work Yet */
        // Filter
        filterButton.setOnClickListener(v -> {
            String[] filterOptions = {"Vegetarian", "Spicy", "Other"};

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Choose filter")
                    .setItems(filterOptions, (dialog, which) -> {
                        String selected = filterOptions[which];
                    })
                    .show();
        });

        searchView.clearFocus();

        return view;
    }
}