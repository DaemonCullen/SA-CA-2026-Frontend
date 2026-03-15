package com.example.sa_ca_2026;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Use 10.0.2.2 for Android emulator instead of localhost
    // Comments out "app.UseHttpRedirection();"
    // Change localhost:5228 -> 10.0.2.2:5228
    private static final String MEALS_API_URL = "http://10.0.2.2:5228/api/Meals";

    private String mParam1;
    private String mParam2;

    ListView listView;
    SearchView searchView;
    Spinner sortSpinner;
    Button filterButton;
    ArrayAdapter<String> arrayAdapter;

    ArrayList<String> allMeals = new ArrayList<>();
    ArrayList<String> displayedMeals = new ArrayList<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

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

    private void loadMealsFromApi() {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(MEALS_API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception("HTTP error code: " + responseCode);
                }

                reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                String json = jsonBuilder.toString();

                JSONArray jsonArray = new JSONArray(json);
                ArrayList<String> mealsFromApi = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject mealObject = jsonArray.getJSONObject(i);

                    String mealName = mealObject.getString("name");
                    mealsFromApi.add(mealName);
                }

                mainHandler.post(() -> {
                    allMeals.clear();
                    allMeals.addAll(mealsFromApi);
                    updateMealList();
                });

            } catch (Exception e) {
                e.printStackTrace();

                final String errorMessage = e.getClass().getSimpleName() + ": " + e.getMessage();

                mainHandler.post(() -> {
                    allMeals.clear();
                    allMeals.add(errorMessage);
                    updateMealList();
                });

            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (Exception ignored) {}

                if (connection != null) {
                    connection.disconnect();
                }
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
                        String selected = filterOptions[which];
                    })
                    .show();
        });

        searchView.clearFocus();

        // Load data from API
        loadMealsFromApi();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}