package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllIngredientsFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Ingredient> ingredientsList = new ArrayList<>();
    private List<String> displayedNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_ingredients, container, false);

        listView = view.findViewById(R.id.ingredientsListView);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, displayedNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            Ingredient selected = ingredientsList.get(position);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, SingleIngredientFragment.newInstance(selected.id))
                    .addToBackStack(null)
                    .commit();
        });

        Spinner filterSpinner = view.findViewById(R.id.ingredientFilterSpinner);
        String[] filterOptions = {"All", "Organic", "Non-Organic"};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        View btnCheap = view.findViewById(R.id.btnCheapIngredients);
        if (btnCheap != null) {
            btnCheap.setOnClickListener(v -> showCheapFilterDialog());
        }

        View btnHighProtein = view.findViewById(R.id.btnHighProtein);
        if (btnHighProtein != null) {
            btnHighProtein.setOnClickListener(v -> showHighProteinFilterDialog());
        }

        View btnLowFat = view.findViewById(R.id.btnLowFat);
        if (btnLowFat != null) {
            btnLowFat.setOnClickListener(v -> showLowFatFilterDialog());
        }

        loadIngredients();

        return view;
    }

    private void loadIngredients() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getAllIngredients().enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ingredient>> call, @NonNull Response<List<Ingredient>> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        ingredientsList.clear();
                        ingredientsList.addAll(response.body());
                        updateList();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Ingredient>> call, @NonNull Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateList() {
        displayedNames.clear();
        for (Ingredient i : ingredientsList) {
            displayedNames.add(i.name + " - " + i.origin + " ($" + i.price + ")");
        }
        adapter.notifyDataSetChanged();
    }

    private void showCheapFilterDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Max Price");
        new AlertDialog.Builder(getContext())
                .setTitle("Filter by Max Price")
                .setView(input)
                .setPositiveButton("Search", (dialog, which) -> {
                    String val = input.getText().toString();
                    if (!val.isEmpty()) {
                        try {
                            filterByPrice(Double.parseDouble(val));
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void filterByPrice(double price) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getIngredientsByMaxPrice(price).enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ingredient>> call, @NonNull Response<List<Ingredient>> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        ingredientsList.clear();
                        ingredientsList.addAll(response.body());
                        updateList();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Ingredient>> call, @NonNull Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showHighProteinFilterDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Min Protein");
        new AlertDialog.Builder(getContext())
                .setTitle("Filter by Min Protein")
                .setView(input)
                .setPositiveButton("Search", (dialog, which) -> {
                    String val = input.getText().toString();
                    if (!val.isEmpty()) {
                        try {
                            filterByProtein(Double.parseDouble(val));
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void filterByProtein(double protein) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getIngredientsByMinProtein(protein).enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ingredient>> call, @NonNull Response<List<Ingredient>> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        ingredientsList.clear();
                        ingredientsList.addAll(response.body());
                        updateList();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Ingredient>> call, @NonNull Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showLowFatFilterDialog() {
        EditText input = new EditText(getContext());
        input.setHint("Max Fat");
        new AlertDialog.Builder(getContext())
                .setTitle("Filter by Max Fat")
                .setView(input)
                .setPositiveButton("Search", (dialog, which) -> {
                    String val = input.getText().toString();
                    if (!val.isEmpty()) {
                        try {
                            filterByFat(Double.parseDouble(val));
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void filterByFat(double fat) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getIngredientsByMinFat(fat).enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<List<Ingredient>> call, @NonNull Response<List<Ingredient>> response) {
                if (isAdded() && getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        ingredientsList.clear();
                        ingredientsList.addAll(response.body());
                        updateList();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Ingredient>> call, @NonNull Throwable t) {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
