package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailFragment extends Fragment {

    private int mealId;
    private Meal currentMeal;

    private TextView nameText, categoryText, ratingText, difficultyText,
            prepTimeText, cookTimeText, servingsText,
            caloriesText, proteinText, totalFatText;

    private Button btnDeleteMeal, btnEditMeal;

    public static MealDetailFragment newInstance(int id) {
        MealDetailFragment fragment = new MealDetailFragment();
        Bundle args = new Bundle();
        args.putInt("meal_id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mealId = getArguments().getInt("meal_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal_detail, container, false);

        nameText = view.findViewById(R.id.detailMealName);
        categoryText = view.findViewById(R.id.detailCategory);
        ratingText = view.findViewById(R.id.detailRating);
        difficultyText = view.findViewById(R.id.detailDifficulty);
        prepTimeText = view.findViewById(R.id.detailPrepTime);
        cookTimeText = view.findViewById(R.id.detailCookTime);
        servingsText = view.findViewById(R.id.detailServings);
        caloriesText = view.findViewById(R.id.detailCalories);
        proteinText = view.findViewById(R.id.detailProtein);
        totalFatText = view.findViewById(R.id.detailTotalFat);

        btnDeleteMeal = view.findViewById(R.id.btnDeleteMeal);
        btnEditMeal = view.findViewById(R.id.btnEditMeal);

        btnDeleteMeal.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Meal")
                    .setMessage("Are you sure you want to delete this meal?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> deleteMealFromApi(mealId))
                    .show();
        });

        btnEditMeal.setOnClickListener(v -> showEditMealDialog());

        loadMealDetails();

        return view;
    }

    private void loadMealDetails() {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);

        api.getMealById(mealId).enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(@NonNull Call<Meal> call, @NonNull Response<Meal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentMeal = response.body();
                    displayMeal(currentMeal);
                } else {
                    Toast.makeText(getContext(), "Failed to load meal details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meal> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteMealFromApi(int id) {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);

        api.deleteMeal(id).enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(@NonNull Call<Meal> call, @NonNull Response<Meal> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Meal deleted", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Failed to delete meal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meal> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateMealInApi(int id, Meal updatedMeal) {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);

        api.updateMeal(id, updatedMeal).enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(@NonNull Call<Meal> call, @NonNull Response<Meal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentMeal = response.body();
                    displayMeal(currentMeal);
                    Toast.makeText(requireContext(), "Meal updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to update meal: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meal> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showEditMealDialog() {
        if (currentMeal == null) {
            Toast.makeText(requireContext(), "Meal data not loaded yet", Toast.LENGTH_SHORT).show();
            return;
        }

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

        editMealName.setText(currentMeal.name);
        editMealCalories.setText(String.valueOf(currentMeal.calories));
        editMealProtein.setText(String.valueOf(currentMeal.protein));
        editMealTotalFat.setText(String.valueOf(currentMeal.totalFat));
        editMealRating.setRating((float) currentMeal.rating);

        editMealPrepTime.setProgress(currentMeal.prepTime);
        editMealCookTime.setProgress(currentMeal.cookTime);
        editMealServings.setProgress(currentMeal.servings - 1);

        textPrepTime.setText("Prep Time: " + currentMeal.prepTime + " mins");
        textCookTime.setText("Cook Time: " + currentMeal.cookTime + " mins");
        textServings.setText("Servings: " + currentMeal.servings);

        for (int i = 0; i < categoryOptions.length; i++) {
            if (categoryOptions[i].equalsIgnoreCase(currentMeal.category)) {
                editMealCategory.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < difficultyOptions.length; i++) {
            if (difficultyOptions[i].equalsIgnoreCase(currentMeal.difficulty)) {
                editMealDifficulty.setSelection(i);
                break;
            }
        }

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
                .setTitle("Edit Meal")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        Meal updatedMeal = new Meal();
                        updatedMeal.name = editMealName.getText().toString().trim();
                        updatedMeal.category = editMealCategory.getSelectedItem().toString();
                        updatedMeal.difficulty = editMealDifficulty.getSelectedItem().toString();
                        updatedMeal.rating = editMealRating.getRating();
                        updatedMeal.prepTime = editMealPrepTime.getProgress();
                        updatedMeal.cookTime = editMealCookTime.getProgress();
                        updatedMeal.servings = editMealServings.getProgress() + 1;
                        updatedMeal.calories = Integer.parseInt(editMealCalories.getText().toString().trim());
                        updatedMeal.protein = Double.parseDouble(editMealProtein.getText().toString().trim());
                        updatedMeal.totalFat = Double.parseDouble(editMealTotalFat.getText().toString().trim());

                        if (updatedMeal.name.isEmpty()) {
                            Toast.makeText(requireContext(), "Please enter a meal name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        updateMealInApi(mealId, updatedMeal);

                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void displayMeal(Meal meal) {
        nameText.setText(meal.name);
        categoryText.setText(meal.category);
        ratingText.setText(String.valueOf(meal.rating));
        difficultyText.setText(meal.difficulty);
        prepTimeText.setText(meal.prepTime + " mins");
        cookTimeText.setText(meal.cookTime + " mins");
        servingsText.setText(String.valueOf(meal.servings));
        caloriesText.setText(meal.calories + " kcal");
        proteinText.setText(meal.protein + "g");
        totalFatText.setText(meal.totalFat + "g");
    }
}