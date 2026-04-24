package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllIngredientsFragment extends Fragment {

    private ListView listView;
    private SearchView searchView;
    private Spinner filterSpinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<Ingredient> allIngredientsList = new ArrayList<>();
    private ArrayList<String> displayedNames = new ArrayList<>();

    private Button btnCheapIngredients, btnHighProtein, btnLowFat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_ingredients, container, false);

        listView = view.findViewById(R.id.ingredientsListView);
        searchView = view.findViewById(R.id.ingredientSearchView);
        filterSpinner = view.findViewById(R.id.ingredientFilterSpinner);

        btnCheapIngredients = view.findViewById(R.id.btnCheapIngredients);
        btnHighProtein = view.findViewById(R.id.btnHighProtein);
        btnLowFat = view.findViewById(R.id.btnLowFat);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, displayedNames);
        listView.setAdapter(adapter);

        setupFilterSpinner();
        setupSearch();
        setupButtons();
        setupListClick();

        loadAllIngredients();

        return view;
    }

    private void setupButtons() {
        btnCheapIngredients.setOnClickListener(v -> showPriceDialog("cheap"));
        btnHighProtein.setOnClickListener(v -> showPriceDialog("protein"));
        btnLowFat.setOnClickListener(v -> showPriceDialog("fat"));
    }

    private void showPriceDialog(String filterType) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter_number, null);
        EditText editNumber = dialogView.findViewById(R.id.editFilterNumber);

        String title, hint;
        if (filterType.equals("cheap")) {
            title = "Max Price";
            hint = "e.g., 20";
        } else if (filterType.equals("protein")) {
            title = "Min Protein";
            hint = "e.g., 20";
        } else {
            title = "Max Fat";
            hint = "e.g., 10";
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Filter by " + title)
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Filter", (dialog, which) -> {
                    try {
                        double value = Double.parseDouble(editNumber.getText().toString().trim());
                        if (filterType.equals("cheap")) {
                            filterByMaxPrice(value);
                        } else if (filterType.equals("protein")) {
                            filterByMinProtein(value);
                        } else {
                            filterByMinFat(value);
                        }
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void filterByMaxPrice(double maxPrice) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getIngredientsByMaxPrice(maxPrice).enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allIngredientsList.clear();
                    allIngredientsList.addAll(response.body());
                    updateAdapter();
                    Toast.makeText(getContext(), "Found " + response.body().size() + " ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterByMinProtein(double minProtein) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getIngredientsByMinProtein(minProtein).enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allIngredientsList.clear();
                    allIngredientsList.addAll(response.body());
                    updateAdapter();
                    Toast.makeText(getContext(), "Found " + response.body().size() + " ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterByMinFat(double minFat) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);
        api.getIngredientsByMinFat(minFat).enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allIngredientsList.clear();
                    allIngredientsList.addAll(response.body());
                    updateAdapter();
                    Toast.makeText(getContext(), "Found " + response.body().size() + " ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupFilterSpinner() {
        String[] filterOptions = {"All", "Organic", "Non-Organic"};
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                filterOptions
        );
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(filterAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterIngredients(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchIngredients(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadAllIngredients();
                }
                return true;
            }
        });
    }

    private void setupListClick() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Ingredient ingredient = allIngredientsList.get(position);
            Fragment detailFragment = SingleIngredientFragment.newInstance(ingredient.id);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void loadAllIngredients() {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.getAllIngredients().enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allIngredientsList.clear();
                    allIngredientsList.addAll(response.body());
                    updateAdapter();
                } else {
                    Toast.makeText(getContext(), "Failed to load ingredients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void searchIngredients(String query) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        api.getIngredientByName(query).enqueue(new Callback<java.util.List<Ingredient>>() {
            @Override
            public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allIngredientsList.clear();
                    allIngredientsList.addAll(response.body());
                    updateAdapter();
                }
            }

            @Override
            public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filterIngredients(int filterType) {
        IngredientsApi api = RetrofitClient.getClient().create(IngredientsApi.class);

        if (filterType == 1) {
            api.getOrganic(true).enqueue(new Callback<java.util.List<Ingredient>>() {
                @Override
                public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        allIngredientsList.clear();
                        allIngredientsList.addAll(response.body());
                        updateAdapter();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else if (filterType == 2) {
            api.getOrganic(false).enqueue(new Callback<java.util.List<Ingredient>>() {
                @Override
                public void onResponse(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Response<java.util.List<Ingredient>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        allIngredientsList.clear();
                        allIngredientsList.addAll(response.body());
                        updateAdapter();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<java.util.List<Ingredient>> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            loadAllIngredients();
        }
    }

    private void updateAdapter() {
        displayedNames.clear();
        for (Ingredient ing : allIngredientsList) {
            displayedNames.add(ing.name + " - " + ing.origin + " ($" + ing.price + ")");
        }
        adapter.notifyDataSetChanged();
    }
}