package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientListFragment extends Fragment {

    private int mealId;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Ingredient> ingredientsList = new ArrayList<>();
    private Button btnAddIngredient;

    public static IngredientListFragment newInstance(int mealId) {
        IngredientListFragment fragment = new IngredientListFragment();
        Bundle args = new Bundle();
        args.putInt("meal_id", mealId);
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

        View view = inflater.inflate(R.layout.fragment_ingredient_list, container, false);

        listView = view.findViewById(R.id.ingredientsListView);
        btnAddIngredient = view.findViewById(R.id.btnAddIngredient);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new java.util.ArrayList<>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            Ingredient ingredient = ingredientsList.get(position);
            Fragment detailFragment = SingleIngredientFragment.newInstance(ingredient.id);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        listView.setOnItemLongClickListener((parent, view1, position, id) -> {
            Ingredient ingredient = ingredientsList.get(position);
            new AlertDialog.Builder(requireContext())
                    .setTitle("Remove Ingredient")
                    .setMessage("Remove " + ingredient.name + " from this meal?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Remove", (dialog, which) -> deleteIngredient(ingredient.id))
                    .show();
            return true;
        });

        btnAddIngredient.setOnClickListener(v -> showAddIngredientDialog());

        loadIngredients();

        return view;
    }

    private void loadIngredients() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.getIngredientsByMeal(mealId).enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ingredientsList.clear();
                    ingredientsList.addAll(response.body());

                    if (ingredientsList.isEmpty()) {
                        randomizeIngredients();
                    } else {
                        updateAdapter();
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateAdapter() {
        adapter.clear();
        for (Ingredient ing : ingredientsList) {
            adapter.add(ing.name + " - " + ing.origin + " ($" + ing.price + ")");
        }
        adapter.notifyDataSetChanged();
    }

    private void randomizeIngredients() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.addRandomIngredients(mealId, 3).enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                System.out.println("Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    ingredientsList.clear();
                    ingredientsList.addAll(response.body());
                    updateAdapter();
                    Toast.makeText(getContext(), "Ingredients added!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        System.out.println("Error body: " + response.errorBody().string());
                    } catch (Exception e) {}
                    Toast.makeText(getContext(), "Failed to add ingredients (code: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteIngredient(int id) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.removeIngredient(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Ingredient removed", Toast.LENGTH_SHORT).show();
                    loadIngredients();
                } else {
                    Toast.makeText(getContext(), "Failed to remove ingredient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showAddIngredientDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_ingredient, null);

        EditText editName = dialogView.findViewById(R.id.editIngredientName);
        EditText editOrigin = dialogView.findViewById(R.id.editIngredientOrigin);
        EditText editPrice = dialogView.findViewById(R.id.editIngredientPrice);
        EditText editProtein = dialogView.findViewById(R.id.editIngredientProtein);
        EditText editFats = dialogView.findViewById(R.id.editIngredientFats);

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Ingredient to Meal")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    try {
                        Ingredient newIng = new Ingredient();
                        newIng.name = editName.getText().toString().trim();
                        newIng.origin = editOrigin.getText().toString().trim();
                        newIng.price = Double.parseDouble(editPrice.getText().toString().trim());
                        newIng.protein = Double.parseDouble(editProtein.getText().toString().trim());
                        newIng.fats = Double.parseDouble(editFats.getText().toString().trim());
                        newIng.carbohydrates = 0;
                        newIng.fiber = 0;
                        newIng.caloriesPerServing = 0;
                        newIng.servingSize = 100;
                        newIng.sodiumContent = 0;
                        newIng.isOrganic = false;
                        newIng.isVegetarian = false;
                        newIng.mealId = mealId;

                        if (newIng.name.isEmpty()) {
                            Toast.makeText(requireContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        System.out.println("Sending: name=" + newIng.name + ", origin=" + newIng.origin + ", price=" + newIng.price);
                        addIngredientToMeal(newIng);
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Please enter valid values", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void addIngredientToMeal(Ingredient ingredient) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        System.out.println("Adding ingredient: " + ingredient.name + " to meal: " + mealId);

        api.addIngredientToMeal(mealId, ingredient).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                System.out.println("Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Ingredient added!", Toast.LENGTH_SHORT).show();
                    loadIngredients();
                } else {
                    try {
                        System.out.println("Error: " + response.errorBody().string());
                    } catch (Exception e) {}
                    Toast.makeText(getContext(), "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}