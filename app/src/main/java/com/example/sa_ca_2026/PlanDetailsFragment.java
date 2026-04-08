package com.example.sa_ca_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanDetailsFragment extends Fragment {

    private String planName;
    private String filterType; 
    private double filterValue; 
    private RecyclerView recyclerView;
    private WeeklyMealsAdapter adapter;
    private List<WeeklyMeal> weeklyMeals = new ArrayList<>();


    private static final String ARG_PLAN_NAME = "plan_name";
    private static final String ARG_FILTER_TYPE = "filter_type";
    private static final String ARG_FILTER_VALUE = "filter_value";

    public static PlanDetailsFragment newInstance(String planName) {
        // Updated to pass default values for filterType and filterValue
        return newInstance(planName, "none", -1.0);
    }

    public static PlanDetailsFragment newInstance(String planName, String filterType, double filterValue) {
        PlanDetailsFragment fragment = new PlanDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLAN_NAME, planName);
        args.putString(ARG_FILTER_TYPE, filterType);
        args.putDouble(ARG_FILTER_VALUE, filterValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            planName = getArguments().getString(ARG_PLAN_NAME);
            filterType = getArguments().getString(ARG_FILTER_TYPE);
            filterValue = getArguments().getDouble(ARG_FILTER_VALUE, -1);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_details, container, false);
        
        TextView title = view.findViewById(R.id.textViewPlanDetailTitle);
        title.setText(planName);

        recyclerView = view.findViewById(R.id.recyclerViewWeeklyMeals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new WeeklyMealsAdapter(weeklyMeals);
        recyclerView.setAdapter(adapter);

        loadMealsAndGeneratePlan();

        return view;
    }

    private void loadMealsAndGeneratePlan() {
        MealsApi api = RetrofitClient.getClient().create(MealsApi.class);
        Call<List<Meal>> call;

        // Use the filterType passed from PlansFragment to decide the API call
        if ("protein".equals(filterType) && filterValue >= 0) {
            call = api.getMealsByMinProtein(filterValue);
        } else if ("calories".equals(filterType) && filterValue >= 0) {
            call = api.getMealsByCalories(filterValue);
        } else {
            call = api.getMeals();
        }

        call.enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(@NonNull Call<List<Meal>> call, @NonNull Response<List<Meal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> meals = response.body();
                    if (meals.isEmpty()) {
                        Toast.makeText(getContext(), "No meals found for this plan's criteria", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    generateWeeklyPlan(meals);
                } else {
                    Toast.makeText(getContext(), "Failed to load meals", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Meal>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateWeeklyPlan(List<Meal> allMeals) {
        if (allMeals.isEmpty()) return;

        List<Meal> breakfastMeals = new ArrayList<>();
        List<Meal> lunchMeals = new ArrayList<>();
        List<Meal> dinnerMeals = new ArrayList<>();

        for (Meal meal : allMeals) {
            if ("Breakfast".equalsIgnoreCase(meal.category)) {
                breakfastMeals.add(meal);
            } else if ("Lunch".equalsIgnoreCase(meal.category)) {
                lunchMeals.add(meal);
            } else if ("Dinner".equalsIgnoreCase(meal.category)) {
                dinnerMeals.add(meal);
            }
        }

        // Fall back to all meals if a category has no results
        if (breakfastMeals.isEmpty()) breakfastMeals = allMeals;
        if (lunchMeals.isEmpty()) lunchMeals = allMeals;
        if (dinnerMeals.isEmpty()) dinnerMeals = allMeals;

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Random random = new Random();
        weeklyMeals.clear();

        for (String day : days) {
            Meal breakfast = breakfastMeals.get(random.nextInt(breakfastMeals.size()));
            Meal lunch = lunchMeals.get(random.nextInt(lunchMeals.size()));
            Meal dinner = dinnerMeals.get(random.nextInt(dinnerMeals.size()));
            weeklyMeals.add(new WeeklyMeal(day, breakfast.name, lunch.name, dinner.name));
        }
        adapter.notifyDataSetChanged();
    }

    // Helper classes
    static class WeeklyMeal {
        String day;
        String breakfast;
        String lunch;
        String dinner;

        WeeklyMeal(String day, String breakfast, String lunch, String dinner) {
            this.day = day;
            this.breakfast = breakfast;
            this.lunch = lunch;
            this.dinner = dinner;
        }
    }

    static class WeeklyMealsAdapter extends RecyclerView.Adapter<WeeklyMealsAdapter.ViewHolder> {
        private final List<WeeklyMeal> meals;

        WeeklyMealsAdapter(List<WeeklyMeal> meals) {
            this.meals = meals;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weekly_meal, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            WeeklyMeal item = meals.get(position);
            holder.dayText.setText(item.day);
            holder.breakfastText.setText(item.breakfast);
            holder.lunchText.setText(item.lunch);
            holder.dinnerText.setText(item.dinner);
        }

        @Override
        public int getItemCount() {
            return meals.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView dayText, breakfastText, lunchText, dinnerText;
            ViewHolder(View itemView) {
                super(itemView);
                dayText = itemView.findViewById(R.id.textViewDay);
                breakfastText = itemView.findViewById(R.id.textViewBreakfast);
                lunchText = itemView.findViewById(R.id.textViewLunch);
                dinnerText = itemView.findViewById(R.id.textViewDinner);
            }
        }
    }
}
