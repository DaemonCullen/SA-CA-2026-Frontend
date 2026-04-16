package com.example.sa_ca_2026;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChosenPlanSplashFragment extends Fragment {

    private static final String ARG_TITLE        = "goal_title";
    private static final String ARG_DESCRIPTION  = "description";
    private static final String ARG_PLAN_NAME    = "plan_name";
    private static final String ARG_FILTER_TYPE  = "filter_type";
    private static final String ARG_FILTER_VALUE = "filter_value";

    public static ChosenPlanSplashFragment newInstance(
            String goalTitle, String description,
            String planName, String filterType, double filterValue) {

        ChosenPlanSplashFragment fragment = new ChosenPlanSplashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE,        goalTitle);
        args.putString(ARG_DESCRIPTION,  description);
        args.putString(ARG_PLAN_NAME,    planName);
        args.putString(ARG_FILTER_TYPE,  filterType);
        args.putDouble(ARG_FILTER_VALUE, filterValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chosen_plan_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args == null) return;

        String goalTitle   = args.getString(ARG_TITLE, "");
        String description = args.getString(ARG_DESCRIPTION, "");
        String planName    = args.getString(ARG_PLAN_NAME, "");
        String filterType  = args.getString(ARG_FILTER_TYPE, "none");
        double filterValue = args.getDouble(ARG_FILTER_VALUE, -1);

        ((TextView) view.findViewById(R.id.textViewSplashTitle)).setText(goalTitle);
        ((TextView) view.findViewById(R.id.textViewSplashDescription)).setText(description);

        Button btnGo = view.findViewById(R.id.btnGoToPlan);
        btnGo.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout,
                                PlanDetailsFragment.newInstance(planName, filterType, filterValue))
                        .addToBackStack(null)
                        .commit()
        );
    }
}
