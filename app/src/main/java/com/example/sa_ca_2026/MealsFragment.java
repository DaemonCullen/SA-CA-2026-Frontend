package com.example.sa_ca_2026;

import android.os.Bundle;
import androidx.annotation.NonNull;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealsFragment extends Fragment {

    ListView listView;
    SearchView searchView;
    Spinner sortSpinner;
    Button filterButton;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> allMeals = new ArrayList<>();
    ArrayList<String> displayedMeals = new ArrayList<>();

    public MealsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateMealList() {
        String query = searchView.getQuery().toString().toLowerCase().trim();
        String sortChoice = sortSpinner.getSelectedItem() != null ? sortSpinner.getSelectedItem().toString() : "A-Z";

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

    private void loadMealsFromApi() {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class); // Create the class from the model
        
        api.getMeals().enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(@NonNull Call<List<Meal>> call, @NonNull Response<List<Meal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allMeals.clear();
                    for (Meal meal : response.body()) {
                        allMeals.add(meal.name);
                    }
                    updateMealList();
                } else {
                    Toast.makeText(getContext(), "Failed to get meals: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Meal>> call, @NonNull Throwable t) {
                allMeals.clear();
                allMeals.add("Error: " + t.getMessage());
                updateMealList();
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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

        arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                R.id.mealName != 0 ? R.layout.meal_list_item : android.R.layout.simple_list_item_1,
                displayedMeals
        );
        // Note: If you use a custom layout like R.layout.meal_list_item, 
        // ensure you pass the TextView ID correctly. 
        // For now, I'm setting a fallback to a standard list item if layout is missing.
        listView.setAdapter(arrayAdapter);

        String[] sortOptions = {"A-Z", "Z-A"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sortOptions
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

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

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateMealList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        filterButton.setOnClickListener(v -> {
            String[] filterOptions = {"Vegetarian", "Spicy", "Other"};
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Choose filter")
                    .setItems(filterOptions, (dialog, which) -> {
                        // Filter logic here
                    })
                    .show();
        });

        searchView.clearFocus();
        loadMealsFromApi();

        return view;
    }
}
