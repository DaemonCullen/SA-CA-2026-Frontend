package com.example.sa_ca_2026;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

    ArrayList<Meal> allMealsList = new ArrayList<>();
    ArrayList<String> displayedMealNames = new ArrayList<>();

    public MealsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void updateMealList() {
        String query = searchView.getQuery().toString().toLowerCase().trim();
        String sortChoice = sortSpinner.getSelectedItem() != null ? sortSpinner.getSelectedItem().toString() : "A-Z";

        displayedMealNames.clear();

        for (Meal meal : allMealsList) {
            if (meal.name.toLowerCase().contains(query)) {
                displayedMealNames.add(meal.name);
            }
        }

        Collections.sort(displayedMealNames, String.CASE_INSENSITIVE_ORDER);

        if (sortChoice.equals("Z-A")) {
            Collections.reverse(displayedMealNames);
        }

        arrayAdapter.notifyDataSetChanged();
    }

    private void loadMealsFromApi() {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);
        
        api.getMeals().enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(@NonNull Call<List<Meal>> call, @NonNull Response<List<Meal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allMealsList.clear();
                    allMealsList.addAll(response.body());
                    updateMealList();
                } else {
                    Toast.makeText(getContext(), "Failed to get meals: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Meal>> call, @NonNull Throwable t) {
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
                android.R.layout.simple_list_item_1,
                displayedMealNames
        );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedName = displayedMealNames.get(position);
            Meal selectedMeal = null;
            for (Meal m : allMealsList) {
                if (m.name.equals(selectedName)) {
                    selectedMeal = m;
                    break;
                }
            }

            if (selectedMeal != null) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, MealDetailFragment.newInstance(selectedMeal.id));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

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
                    })
                    .show();
        });

        searchView.clearFocus();
        loadMealsFromApi();

        return view;
    }
}
