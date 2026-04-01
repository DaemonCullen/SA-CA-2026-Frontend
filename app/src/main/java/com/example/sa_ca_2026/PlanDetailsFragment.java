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
    private RecyclerView recyclerView;
    private WeeklyMealsAdapter adapter;
    private List<WeeklyMeal> weeklyMeals = new ArrayList<>();

    public static PlanDetailsFragment newInstance(String planName) {
        PlanDetailsFragment fragment = new PlanDetailsFragment();
        Bundle args = new Bundle();
        args.putString("plan_name", planName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            planName = getArguments().getString("plan_name");
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
        api.getMeals().enqueue(new Callback<List<Meal>>() {
            @Override
            public void onResponse(@NonNull Call<List<Meal>> call, @NonNull Response<List<Meal>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    generateWeeklyPlan(response.body());
                } else {
                    Toast.makeText(getContext(), "Failed to load meals for plan", Toast.LENGTH_SHORT).show();
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

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Random random = new Random();
        weeklyMeals.clear();

        for (String day : days) {
            Meal randomMeal = allMeals.get(random.nextInt(allMeals.size()));
            weeklyMeals.add(new WeeklyMeal(day, randomMeal.name));
        }
        adapter.notifyDataSetChanged();
    }

    // Helper classes
    static class WeeklyMeal {
        String day;
        String mealName;

        WeeklyMeal(String day, String mealName) {
            this.day = day;
            this.mealName = mealName;
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
            holder.mealText.setText(item.mealName);
        }

        @Override
        public int getItemCount() {
            return meals.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView dayText, mealText;
            ViewHolder(View itemView) {
                super(itemView);
                dayText = itemView.findViewById(R.id.textViewDay);
                mealText = itemView.findViewById(R.id.textViewMealName);
            }
        }
    }
}
