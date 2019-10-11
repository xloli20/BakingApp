package com.example.bakingapp.UI;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.Adapters.StepsAdapter;
import com.example.bakingapp.Models.Recipes;
import com.example.bakingapp.Models.RecipesIngredients;
import com.example.bakingapp.Models.RecipesSteps;
import com.example.bakingapp.R;
import com.example.bakingapp.RecipeAppWidget;
import com.example.bakingapp.Utils.JsonUtils;
import com.example.bakingapp.Utils.NetworkUtil;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item sVideoUrl. On tablets, the activity presents the list of items and
 * item sVideoUrl side-by-side using two vertical panes.
 */
public class StepsActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {
    private static final String TAG = StepsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.item_list)
    RecyclerView stepsRecyclerView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.ingredients)
    TextView ingredientsTextView;

    private ArrayList<RecipesIngredients> ingredients;
    private ArrayList<RecipesSteps> steps;

    private Recipes recipes;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        recipes = intent.getParcelableExtra("recipes");

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        stepsRecyclerView.setLayoutManager(linearLayoutManager);

        Log.d(TAG, "onCreate: savedInstanceState " + savedInstanceState);
        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelable("rec");
            if (actionBar != null) {
                actionBar.setTitle(recipes.getrName());
            }
            ingredients = savedInstanceState.getParcelableArrayList("ingre");
            setIngredientsTextView();
            steps = savedInstanceState.getParcelableArrayList("ste");
            setStepsAdapter();
        } else {
            if (actionBar != null) {
                actionBar.setTitle(recipes.getrName());
            }

            URL url = NetworkUtil.buildUrl();
            new StepsQueryTask().execute(url);

            new IngredientsQueryTask().execute(url);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("rec", recipes);
        outState.putParcelableArrayList("ingre", ingredients);
        outState.putParcelableArrayList("ste", steps);
        Log.d(TAG, "onSaveInstanceState: " + recipes + ingredients + steps);
    }

    private void setIngredientsTextView() {
        ingredientsTextView.setText("");
        for (int i = 0; i <= ingredients.size() - 1; i++) {
            ingredientsTextView.append(ingredients.get(i).getrIngredient());
            ingredientsTextView.append(" ");
            ingredientsTextView.append(ingredients.get(i).getiQuantity());
            ingredientsTextView.append(" ");
            ingredientsTextView.append(ingredients.get(i).getiMeasure());
            ingredientsTextView.append("\n");
        }
    }

    private void setStepsAdapter() {
        Log.d(TAG, "setStepsAdapter: " + steps);
        StepsAdapter stepsAdapter = new StepsAdapter(steps, this);
        stepsRecyclerView.setHasFixedSize(true);
        stepsRecyclerView.setAdapter(stepsAdapter);
    }
    private void addWidget() {
        StringBuilder ingredient = new StringBuilder();
        for (int i = 0; i <= ingredients.size() - 1; i++) {
            ingredient.append(ingredients.get(i).getrIngredient());
            ingredient.append(" ");
            ingredient.append(ingredients.get(i).getiQuantity());
            ingredient.append(" ");
            ingredient.append(ingredients.get(i).getiMeasure());
            ingredient.append("\n");
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));
        if (appWidgetIds.length == 0) {
            Toast.makeText(this, "Please make a home screen widget first!", Toast.LENGTH_SHORT).show();
        } else {
            for (int appWidgetId : appWidgetIds) {
                RecipeAppWidget.updateAppWidget(this, appWidgetManager, appWidgetId, recipes.getrName(), ingredient.toString());
            }
        }
    }

    @Override
    public void onListClickItem(int clickedItemIndex) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString("step", steps.get(clickedItemIndex).getsInstructions());
            arguments.putString("video", steps.get(clickedItemIndex).getsVideoUrl());
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra("steps", steps.get(clickedItemIndex));
            intent.putParcelableArrayListExtra("step_list", steps);
            startActivity(intent);
        }
    }

    @OnClick({R.id.toolbar, R.id.app_bar, R.id.ingredients, R.id.item_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.app_bar:
                break;
            case R.id.ingredients:
                break;
            case R.id.item_list:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.widget,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.widget){
            addWidget();
        }
        return super.onOptionsItemSelected(item);
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
                    ingredients = JsonUtils.parseIngredientsJson(s, Integer.valueOf(recipes.getrId()));
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
                    steps = JsonUtils.parseStepsJson(s, Integer.valueOf(recipes.getrId()));
                    setStepsAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }
}
