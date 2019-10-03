package com.example.bakingapp.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.bakingapp.Models.RecipesSteps;
import com.example.bakingapp.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link StepsActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    private static final String TAG = ItemDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy sDescription this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy sDescription specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load sDescription from a sDescription provider.

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(" ");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        TextView instructionTextView = rootView.findViewById(R.id.instruction);

        if(getArguments().containsKey("step")){
            String step = getArguments().getString("step");
            instructionTextView.setText(step);
        }else {
            Intent intent = getActivity().getIntent();
            RecipesSteps steps = intent.getParcelableExtra("steps");
            instructionTextView.setText(steps.getsInstructions());
        }
        return rootView;
    }
}
