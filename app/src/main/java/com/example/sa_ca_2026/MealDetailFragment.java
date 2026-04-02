package com.example.sa_ca_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private TextView nameText, categoryText, ratingText, difficultyText, prepTimeText, cookTimeText, servingsText, caloriesText, proteinText, totalFatText;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        Button btnBack = view.findViewById(R.id.btnBackToMeals);
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        loadMealDetails();

        return view;
    }

    private void loadMealDetails() {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);
        api.getMealById(mealId).enqueue(new Callback<Meal>() {
            @Override
            public void onResponse(@NonNull Call<Meal> call, @NonNull Response<Meal> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayMeal(response.body());
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
