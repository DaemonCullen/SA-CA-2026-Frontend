package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleIngredientFragment extends Fragment {

    private int ingredientId;
    private Ingredient currentIngredient;

    private TextView nameText, originText, priceText, organicText,
            fatsText, proteinText, carbsText, fiberText,
            vegText, caloriesText, servingSizeText, sodiumText;

    private Button btnDeleteIngredient, btnEditIngredient;

    public static SingleIngredientFragment newInstance(int id) {
        SingleIngredientFragment fragment = new SingleIngredientFragment();
        Bundle args = new Bundle();
        args.putInt("ingredient_id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ingredientId = getArguments().getInt("ingredient_id");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_single_ingredient, container, false);

        nameText = view.findViewById(R.id.ingredientName);
        originText = view.findViewById(R.id.ingredientOrigin);
        priceText = view.findViewById(R.id.ingredientPrice);
        organicText = view.findViewById(R.id.ingredientOrganic);
        fatsText = view.findViewById(R.id.ingredientFats);
        proteinText = view.findViewById(R.id.ingredientProtein);
        carbsText = view.findViewById(R.id.ingredientCarbs);
        fiberText = view.findViewById(R.id.ingredientFiber);
        vegText = view.findViewById(R.id.ingredientVeg);
        caloriesText = view.findViewById(R.id.ingredientCalories);
        servingSizeText = view.findViewById(R.id.ingredientServingSize);
        sodiumText = view.findViewById(R.id.ingredientSodium);

        btnDeleteIngredient = view.findViewById(R.id.btnDeleteIngredient);
        btnEditIngredient = view.findViewById(R.id.btnEditIngredient);

        btnDeleteIngredient.setOnClickListener(v -> showDeleteDialog());
        btnEditIngredient.setOnClickListener(v -> showEditDialog());

        loadIngredient();

        return view;
    }

    private void loadIngredient() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.getIngredientById(ingredientId).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentIngredient = response.body();
                    displayIngredient(currentIngredient);
                } else {
                    Toast.makeText(getContext(), "Failed to load ingredient", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayIngredient(Ingredient ing) {
        nameText.setText(ing.name);
        originText.setText(ing.origin);
        priceText.setText("$" + ing.price);
        organicText.setText(ing.isOrganic ? "Yes" : "No");
        fatsText.setText(ing.fats + "g");
        proteinText.setText(ing.protein + "g");
        carbsText.setText(ing.carbohydrates + "g");
        fiberText.setText(ing.fiber + "g");
        vegText.setText(ing.isVegetarian ? "Yes" : "No");
        caloriesText.setText(ing.caloriesPerServing + " kcal");
        servingSizeText.setText(ing.servingSize + "g");
        sodiumText.setText(ing.sodiumContent + "mg");
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.ingredientsDetails_page_deleteIngredient))
                .setMessage(getString(R.string.ingredientsDetails_page_deleteIngredient) + currentIngredient.name + "?")
                .setNegativeButton(getString(R.string.ingredientsDetails_page_cancel), null)
                .setPositiveButton(getString(R.string.ingredientsDetails_page_delete), (dialog, which) -> deleteIngredient())
                .show();
    }

    private void deleteIngredient() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.removeIngredient(ingredientId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), getString(R.string.editIngredient_dialog_updated), Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.editIngredient_dialog_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showEditDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_ingredient, null);

        EditText editName = dialogView.findViewById(R.id.editIngredientName);
        EditText editOrigin = dialogView.findViewById(R.id.editIngredientOrigin);
        EditText editPrice = dialogView.findViewById(R.id.editIngredientPrice);
        EditText editProtein = dialogView.findViewById(R.id.editIngredientProtein);
        EditText editFats = dialogView.findViewById(R.id.editIngredientFats);

        editName.setText(currentIngredient.name);
        editOrigin.setText(currentIngredient.origin);
        editPrice.setText(String.valueOf(currentIngredient.price));
        editProtein.setText(String.valueOf(currentIngredient.protein));
        editFats.setText(String.valueOf(currentIngredient.fats));

        new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.editIngredient_dialog_edit))
                .setView(dialogView)
                .setNegativeButton(getString(R.string.editIngredient_dialog_cancel), null)
                .setPositiveButton(getString(R.string.editIngredient_dialog_save), (dialog, which) -> {
                    try {
                        currentIngredient.name = editName.getText().toString().trim();
                        currentIngredient.origin = editOrigin.getText().toString().trim();
                        currentIngredient.price = Double.parseDouble(editPrice.getText().toString().trim());
                        currentIngredient.protein = Double.parseDouble(editProtein.getText().toString().trim());
                        currentIngredient.fats = Double.parseDouble(editFats.getText().toString().trim());

                        updateIngredient();
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), getString(R.string.editIngredient_dialog_values), Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    private void updateIngredient() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.updateIngredient(ingredientId, currentIngredient).enqueue(new Callback<Ingredient>() {
            @Override
            public void onResponse(@NonNull Call<Ingredient> call, @NonNull Response<Ingredient> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentIngredient = response.body();
                    displayIngredient(currentIngredient);
                    Toast.makeText(requireContext(), getString(R.string.editIngredient_dialog_updated), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), getString(R.string.editIngredient_dialog_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Ingredient> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}