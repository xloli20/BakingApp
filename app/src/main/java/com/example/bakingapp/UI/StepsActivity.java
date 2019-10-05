package com.example.bakingapp.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bakingapp.Adapters.StepsAdapter;
import com.example.bakingapp.Models.Recipes;
import com.example.bakingapp.Models.RecipesIngredients;
import com.example.bakingapp.Models.RecipesSteps;
import com.example.bakingapp.R;
import com.example.bakingapp.Utils.JsonUtils;
import com.example.bakingapp.Utils.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item sVideoUrl. On tablets, the activity presents the list of items and
 * item sVideoUrl side-by-side using two vertical panes.
 */
public class StepsActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {
    private static final String TAG = StepsActivity.class.getSimpleName() ;

    private ArrayList<RecipesIngredients> ingredients;
    private TextView ingredientsTextView;

    private ArrayList<RecipesSteps> steps;
    private RecyclerView stepsRecyclerView;
    private Recipes recipes;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private StepsActivity mParentActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final Intent intent = getIntent();
        recipes = intent.getParcelableExtra("recipes");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(recipes.getrName());
        }

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        stepsRecyclerView = findViewById(R.id.item_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        stepsRecyclerView.setLayoutManager(linearLayoutManager);

        URL url = NetworkUtil.buildUrl();
        new StepsQueryTask().execute(url);

        new IngredientsQueryTask().execute(url);
    }

    private void setIngredientsTextView(){
        ingredientsTextView = findViewById(R.id.ingredients);
        ingredientsTextView.setText("");
        for (int i=0 ; i<=9 ; i++){
            ingredientsTextView.append(ingredients.get(i).getrIngredient());
            ingredientsTextView.append(" ");
            ingredientsTextView.append(ingredients.get(i).getiQuantity());
            ingredientsTextView.append(" ");
            ingredientsTextView.append(ingredients.get(i).getiMeasure());
            ingredientsTextView.append("\n");
        }
    }

    private void setStepsAdapter() {
        Log.d(TAG, "setStepsAdapter: " +steps);
        StepsAdapter stepsAdapter = new StepsAdapter(steps,this);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }

    @Override
    public void onListClickItem(int clickedItemIndex) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString("step",steps.get(clickedItemIndex).getsInstructions());
            arguments.putString("video",steps.get(clickedItemIndex).getsVideoUrl());
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("steps",steps.get(clickedItemIndex));
            startActivity(intent);
        }
    }

    public class IngredientsQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String mResult = null;
            try {
                mResult = NetworkUtil.getResponseFromHttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mResult;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null && !s.equals("")) {

                try {
                    Log.d(TAG, "onPostExecute: steps "+ingredients);
                    Log.d(TAG, "onPostExecute: recipes.getrId() "+recipes.getrId());

                    ingredients = JsonUtils.parseIngredientsJson(s,Integer.valueOf(recipes.getrId()));
                    Log.d(TAG, "onPostExecute: steps "+ingredients);
                    setIngredientsTextView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
            }
        }
    }

    public class StepsQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String mResult = null;
            try {
                mResult = NetworkUtil.getResponseFromHttpURL(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mResult;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null && !s.equals("")) {
                stepsRecyclerView.setVisibility(View.VISIBLE);

                try {

                    steps = JsonUtils.parseStepsJson(s,Integer.valueOf(recipes.getrId()));
                    Log.d(TAG, "onPostExecute: steps "+steps);
                    setStepsAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
            }
        }
    }
}
