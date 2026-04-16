package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlansFragment extends Fragment {

    private RecyclerView listViewPlans;
    private List<Plan> plans = new ArrayList<>();
    private PlansAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewPlans = view.findViewById(R.id.listViewPlans);

        adapter = new PlansAdapter(plans,
            // click -> navigate to details
            plan -> {
                if (plan.name.equals("Muscle Building Plan")) {
                    showFilterDialog(plan.name, "protein", "Minimum Protein (g)");
                } else if (plan.name.equals("Fat Loss Plan")) {
                    showFilterDialog(plan.name, "calories", "Maximum Calories");
                } else {
                    navigateToPlanDetails(plan.name, "none", -1);
                }
            },
            // long click -> confirm delete
            plan -> showDeleteDialog(plan)
        );

        listViewPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        listViewPlans.setAdapter(adapter);

        loadPlans();

        view.findViewById(R.id.bannerInputGoals).setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, new GoalsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        Button btnCreatePlan = view.findViewById(R.id.btnCreatePlan);
        btnCreatePlan.setOnClickListener(v -> showCreateDialog());
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    private void loadPlans() {
        PlansApi api = RetrofitClient.getClient().create(PlansApi.class);
        api.getAllPlans().enqueue(new Callback<List<Plan>>() {
            @Override
            public void onResponse(@NonNull Call<List<Plan>> call, @NonNull Response<List<Plan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    plans.clear();
                    plans.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load plans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Plan>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    private void showCreateDialog() {
        EditText nameInput = new EditText(getContext());
        nameInput.setHint("Plan name");

        EditText descInput = new EditText(getContext());
        descInput.setHint("Description (optional)");

        // stack the two inputs vertically inside the dialog
        android.widget.LinearLayout layout = new android.widget.LinearLayout(getContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(48, 16, 48, 0);
        layout.addView(nameInput);
        layout.addView(descInput);

        new AlertDialog.Builder(getContext())
                .setTitle("Create Plan")
                .setView(layout)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String desc = descInput.getText().toString().trim();
                    if (!name.isEmpty()) {
                        createPlan(name, desc);
                    } else {
                        Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void createPlan(String name, String description) {
        Plan newPlan = new Plan();
        newPlan.name = name;
        newPlan.description = description;

        PlansApi api = RetrofitClient.getClient().create(PlansApi.class);
        api.createPlan(newPlan).enqueue(new Callback<Plan>() {
            @Override
            public void onResponse(@NonNull Call<Plan> call, @NonNull Response<Plan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    plans.add(response.body());          // add the DB version (has real id)
                    adapter.notifyItemInserted(plans.size() - 1);
                    Toast.makeText(getContext(), "Plan created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to create plan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Plan> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    private void showDeleteDialog(Plan plan) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Plan")
                .setMessage("Delete \"" + plan.name + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> deletePlan(plan))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deletePlan(Plan plan) {
        PlansApi api = RetrofitClient.getClient().create(PlansApi.class);
        api.deletePlan(plan.id).enqueue(new Callback<Plan>() {
            @Override
            public void onResponse(@NonNull Call<Plan> call, @NonNull Response<Plan> response) {
                if (response.isSuccessful()) {
                    int pos = plans.indexOf(plan);
                    plans.remove(plan);
                    adapter.notifyItemRemoved(pos);
                    Toast.makeText(getContext(), "Plan deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to delete plan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Plan> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ── NAVIGATION ────────────────────────────────────────────────────────────

    private void showFilterDialog(String planName, String type, String title) {
        EditText input = new EditText(getContext());
        input.setHint("e.g. 30");
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage("Enter the " + title.toLowerCase() + " for this plan:")
                .setView(input)
                .setPositiveButton("Generate", (dialog, which) -> {
                    String raw = input.getText().toString().trim();
                    double val = raw.isEmpty() ? 0 : Double.parseDouble(raw);
                    navigateToPlanDetails(planName, type, val);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void navigateToPlanDetails(String planName, String filterType, double filterValue) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PlanDetailsFragment.newInstance(planName, filterType, filterValue));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // ── ADAPTER ───────────────────────────────────────────────────────────────

    public interface OnPlanClickListener {
        void onPlanClick(Plan plan);
    }

    public interface OnPlanLongClickListener {
        void onPlanLongClick(Plan plan);
    }

    static class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlansViewHolder> {
        private final List<Plan> plans;
        private final OnPlanClickListener clickListener;
        private final OnPlanLongClickListener longClickListener;

        PlansAdapter(List<Plan> plans, OnPlanClickListener clickListener, OnPlanLongClickListener longClickListener) {
            this.plans = plans;
            this.clickListener = clickListener;
            this.longClickListener = longClickListener;
        }

        @NonNull
        @Override
        public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PlansViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlansViewHolder holder, int position) {
            Plan plan = plans.get(position);
            holder.textViewPlan.setText(plan.name);
            holder.itemView.setOnClickListener(v -> clickListener.onPlanClick(plan));
            holder.itemView.setOnLongClickListener(v -> {
                longClickListener.onPlanLongClick(plan);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return plans.size();
        }

        static class PlansViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView textViewPlan;
            PlansViewHolder(View itemView) {
                super(itemView);
                textViewPlan = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
