package com.example.sa_ca_2026;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlansFragment extends Fragment {




    private RecyclerView listViewPlans;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewPlans = view.findViewById(R.id.listViewPlans);

        List<String> plans = new ArrayList<>();
        plans.add("Fat Loss Plan");
        plans.add("Muscle Building Plan");
        plans.add("Health Plan");

        PlansAdapter adapter = new PlansAdapter(plans, planName -> {
            if (planName.equals("Muscle Building Plan")) {
                showInputDialog(planName, "protein", "Minimum Protein (g)");
            } else if (planName.equals("Fat Loss Plan")) {
                showInputDialog(planName, "calories", "Maximum Calories");
            } else {
                navigateToPlanDetails(planName, "none", -1);
            }
        });

        listViewPlans.setLayoutManager(new LinearLayoutManager(getContext()));
        listViewPlans.setAdapter(adapter);

        Button btnCreatePlan = view.findViewById(R.id.btnCreatePlan);
        btnCreatePlan.setOnClickListener(v -> {
            EditText input = new EditText(getContext());
            input.setHint("Plan name");
            new AlertDialog.Builder(getContext())
                    .setTitle("Create Plan")
                    .setView(input)
                    .setPositiveButton("Create", (dialog, which) -> {
                        String name = input.getText().toString().trim();
                        if (!name.isEmpty()) {
                            plans.add(name);
                            adapter.notifyItemInserted(plans.size() - 1);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void showInputDialog(String planName, String type, String title) {
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

    public interface OnPlanClickListener {
        void onPlanClick(String planName);
    }

    static class PlansAdapter extends RecyclerView.Adapter<PlansAdapter.PlansViewHolder> {
        private final List<String> plans;
        private final OnPlanClickListener listener;

        public PlansAdapter(List<String> plans, OnPlanClickListener listener) {
            this.plans = plans;
            this.listener = listener;
        }

        @NonNull
        @Override
        public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PlansViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlansViewHolder holder, int position) {
            String plan = plans.get(position);
            holder.textViewPlan.setText(plan);
            holder.itemView.setOnClickListener(v -> listener.onPlanClick(plan));
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
