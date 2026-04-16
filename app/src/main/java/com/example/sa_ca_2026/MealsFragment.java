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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;

import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class MealsFragment extends Fragment {

    ListView listView;
    SearchView searchView;
    Spinner sortSpinner;
    Button filterButton;
    ArrayAdapter<String> arrayAdapter;
    // Lists for data
    ArrayList<Meal> allMealsList = new ArrayList<>();
    ArrayList<String> displayedMealNames = new ArrayList<>();

    // Filter State
    String selectedFilterType = "None";
    String selectedFilterValue = "";

    // Constructor (Don't delete)
    public MealsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // ===================
    // Filter Menu
    // ===================
    public class FilterOption {
        public String title;
        public boolean isHeader;

        public FilterOption(String title, boolean isHeader) {
            this.title = title;
            this.isHeader = isHeader;
        }

        @NonNull
        @Override
        public String toString() {
            return title;
        }
    }
    // ===================
    // Filter Adapter (For dividers)
    // ===================
    public class FilterOptionAdapter extends ArrayAdapter<FilterOption> {

        public FilterOptionAdapter(@NonNull Context context, @NonNull List<FilterOption> options) {
            super(context, android.R.layout.simple_list_item_1, options);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = view.findViewById(android.R.id.text1);

            FilterOption option = getItem(position);

            if (option != null) {
                textView.setText(option.title);

                if (option.isHeader) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setEnabled(false);
                    textView.setAlpha(0.7f);
                } else {
                    textView.setTypeface(null, Typeface.NORMAL);
                    textView.setEnabled(true);
                    textView.setAlpha(1f);
                }
            }

            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            FilterOption option = getItem(position);
            return option != null && !option.isHeader;
        }
    }
    // ===================
    // Have a guess
    // ===================
    private void updateMealList() {
        String query = searchView.getQuery().toString().toLowerCase().trim();
        String sortChoice = sortSpinner.getSelectedItem() != null
                ? sortSpinner.getSelectedItem().toString()
                : "A-Z";

        displayedMealNames.clear();

        for (Meal meal : allMealsList) {
            boolean matchesSearch = meal.name != null &&
                    meal.name.toLowerCase().contains(query);

            boolean matchesFilter = true;

            if (selectedFilterType.equals("Category")) {
                matchesFilter = meal.category != null &&
                        meal.category.equalsIgnoreCase(selectedFilterValue);
            }
            else if (selectedFilterType.equals("Difficulty")) {
                matchesFilter = meal.difficulty != null &&
                        meal.difficulty.equalsIgnoreCase(selectedFilterValue);
            }
            else if (selectedFilterType.equals("MinProtein")) {
                try {
                    double minProtein = Double.parseDouble(selectedFilterValue);
                    matchesFilter = meal.protein >= minProtein;
                } catch (NumberFormatException e) {
                    matchesFilter = true;
                }
            }
            else if (selectedFilterType.equals("MaxCalories")) {
                try {
                    double maxCalories = Double.parseDouble(selectedFilterValue);
                    matchesFilter = meal.calories <= maxCalories;
                } catch (NumberFormatException e) {
                    matchesFilter = true;
                }
            }

            if (matchesSearch && matchesFilter) {
                displayedMealNames.add(meal.name);
            }
        }

        Collections.sort(displayedMealNames, String.CASE_INSENSITIVE_ORDER);

        if (sortChoice.equals("Z-A")) {
            Collections.reverse(displayedMealNames);
        }

        arrayAdapter.notifyDataSetChanged();
    }
    // ===================
    // Again guess
    // ===================
    private void showAddMealDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_meal, null);

        EditText editMealName = dialogView.findViewById(R.id.editMealName);
        Spinner editMealCategory = dialogView.findViewById(R.id.editMealCategory);
        Spinner editMealDifficulty = dialogView.findViewById(R.id.editMealDifficulty);
        RatingBar editMealRating = dialogView.findViewById(R.id.editMealRating);
        SeekBar editMealPrepTime = dialogView.findViewById(R.id.editMealPrepTime);
        SeekBar editMealCookTime = dialogView.findViewById(R.id.editMealCookTime);
        SeekBar editMealServings = dialogView.findViewById(R.id.editMealServings);
        EditText editMealCalories = dialogView.findViewById(R.id.editMealCalories);
        EditText editMealProtein = dialogView.findViewById(R.id.editMealProtein);
        EditText editMealTotalFat = dialogView.findViewById(R.id.editMealTotalFat);

        TextView textPrepTime = dialogView.findViewById(R.id.textPrepTime);
        TextView textCookTime = dialogView.findViewById(R.id.textCookTime);
        TextView textServings = dialogView.findViewById(R.id.textServings);

        String[] categoryOptions = {"Breakfast", "Lunch", "Dinner"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryOptions
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editMealCategory.setAdapter(categoryAdapter);

        String[] difficultyOptions = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                difficultyOptions
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editMealDifficulty.setAdapter(difficultyAdapter);

        editMealPrepTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textPrepTime.setText("Prep Time: " + progress + " mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        editMealCookTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textCookTime.setText("Cook Time: " + progress + " mins");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        editMealServings.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textServings.setText("Servings: " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Add New Meal")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        String name = editMealName.getText().toString().trim();
                        String category = editMealCategory.getSelectedItem().toString();
                        String difficulty = editMealDifficulty.getSelectedItem().toString();

                        double rating = editMealRating.getRating();
                        int prepTime = editMealPrepTime.getProgress();
                        int cookTime = editMealCookTime.getProgress();
                        int servings = editMealServings.getProgress() + 1;
                        int calories = Integer.parseInt(editMealCalories.getText().toString().trim());
                        double protein = Double.parseDouble(editMealProtein.getText().toString().trim());
                        double totalFat = Double.parseDouble(editMealTotalFat.getText().toString().trim());

                        if (name.isEmpty()) {
                            Toast.makeText(requireContext(), "Please fill in the meal name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Meal newMeal = new Meal();
                        newMeal.name = name;
                        newMeal.category = category;
                        newMeal.difficulty = difficulty;
                        newMeal.rating = rating;
                        newMeal.prepTime = prepTime;
                        newMeal.cookTime = cookTime;
                        newMeal.servings = servings;
                        newMeal.calories = calories;
                        newMeal.protein = protein;
                        newMeal.totalFat = totalFat;

                        createMealInApi(newMeal);

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Please enter valid values in every field", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }
    private void createMealInApi(Meal newMeal) {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);

        api.createMeal(newMeal).enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(@NonNull Call<Meal> call, @NonNull Response<Meal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Meal added successfully", Toast.LENGTH_SHORT).show();
                    loadMealsFromApi(); // refresh list
                } else {
                    Toast.makeText(requireContext(), "Failed to add meal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meal> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
    // ===================
    // Creates fragment view
    // ===================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meals, container, false);

        // XMLs
        listView = view.findViewById(R.id.mealsList);
        searchView = view.findViewById(R.id.searchView);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        filterButton = view.findViewById(R.id.filterButton);

        // Search setup
        searchView.setQueryHint("Search meals");
        searchView.setIconifiedByDefault(false);

        // List adapter setup
        arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                displayedMealNames
        );

        // Inflate footer layout
        View footerView = inflater.inflate(R.layout.list_add_meal, listView, false);

        // Find footer button
        Button addMealButton = footerView.findViewById(R.id.addMealButton);

        // Add footer to ListView
        listView.addFooterView(footerView);

        // Set adapter after footer is added
        listView.setAdapter(arrayAdapter);

        // Footer button click
        addMealButton.setOnClickListener(v -> showAddMealDialog());

        // Makes list items clickable (MealDetailFragment)
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Ignore footer clicks
            if (position >= displayedMealNames.size()) {
                return;
            }

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

        // Sort setup
        String[] sortOptions = {"A-Z", "Z-A"};
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                sortOptions
        );
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        // Listens for search changes
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

        // Listens for sort changes
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateMealList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Listens for filter changes
        filterButton.setOnClickListener(v -> {
            ArrayList<FilterOption> options = new ArrayList<>();
            options.add(new FilterOption("No Filter", false));

            options.add(new FilterOption("--- Category ---", true));
            options.add(new FilterOption("Breakfast", false));
            options.add(new FilterOption("Lunch", false));
            options.add(new FilterOption("Dinner", false));

            options.add(new FilterOption("--- Difficulty ---", true));
            options.add(new FilterOption("Easy", false));
            options.add(new FilterOption("Medium", false));
            options.add(new FilterOption("Hard", false));

            options.add(new FilterOption("--- Other ---", true));
            options.add(new FilterOption("Protein >= 20g", false));
            options.add(new FilterOption("Calories <= 500", false)); // Cal / Kal IDK

            FilterOptionAdapter adapter = new FilterOptionAdapter(requireContext(), options);

            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Choose filter")
                    .setAdapter(adapter, (dialog, which) -> {
                        FilterOption selected = options.get(which);

                        if (selected.isHeader) {
                            return;
                        }

                        switch (selected.title) {
                            case "No Filter":
                                selectedFilterType = "None";
                                selectedFilterValue = "";
                                break;

                            case "Breakfast":
                            case "Lunch":
                            case "Dinner":
                                selectedFilterType = "Category";
                                selectedFilterValue = selected.title;
                                break;

                            case "Easy":
                            case "Medium":
                            case "Hard":
                                selectedFilterType = "Difficulty";
                                selectedFilterValue = selected.title;
                                break;

                            case "Protein >= 20g":
                                selectedFilterType = "MinProtein";
                                selectedFilterValue = "20";
                                break;

                            case "Calories <= 500":
                                selectedFilterType = "MaxCalories";
                                selectedFilterValue = "500";
                                break;
                        }

                        updateMealList();
                    })
                    .show();
        });

        searchView.clearFocus();
        loadMealsFromApi();

        return view;
    }
}
