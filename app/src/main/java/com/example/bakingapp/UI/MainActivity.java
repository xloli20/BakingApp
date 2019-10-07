package com.example.bakingapp.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bakingapp.Adapters.RecipesAdapter;
import com.example.bakingapp.Models.Recipes;
import com.example.bakingapp.R;
import com.example.bakingapp.Utils.JsonUtils;
import com.example.bakingapp.Utils.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    ArrayList<Recipes> recipes = new ArrayList<>();
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindView(R.id.recipes_recycler_view)
    RecyclerView recipesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        errorMessage = findViewById(R.id.error_message);
        recipesRecyclerView = findViewById(R.id.recipes_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns());
        recipesRecyclerView.setLayoutManager(gridLayoutManager);

        URL url = NetworkUtil.buildUrl();
        Log.d(TAG, "onCreate: url " + url);
        new RecipesQueryTask().execute(url);

        setRecipesAdapter();
    }

    private void setRecipesAdapter() {
        RecipesAdapter recipesAdapter = new RecipesAdapter(recipes, this);
        recipesRecyclerView.setHasFixedSize(true);
        recipesRecyclerView.setAdapter(recipesAdapter);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 800;
        int width = displayMetrics.widthPixels;
        return width / widthDivider;
    }

    @Override
    public void onListClickItem(int clickedItemIndex) {
        Intent intent = new Intent(this, StepsActivity.class);
        Log.d(TAG, "onListClickItem: recipes " + recipes.get(clickedItemIndex).getrId());
        intent.putExtra("recipes", recipes.get(clickedItemIndex));
        startActivity(intent);
    }

    @OnClick({R.id.error_message, R.id.recipes_recycler_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_message:
            case R.id.recipes_recycler_view:
                break;
        }
    }

    public class RecipesQueryTask extends AsyncTask<URL, Void, String> {

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
                showJsonData();
                try {
                    recipes = JsonUtils.parseRecipesJson(s);
                    setRecipesAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorMassage();
            }
        }
    }

    private void showErrorMassage() {
        recipesRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showJsonData() {
        errorMessage.setVisibility(View.INVISIBLE);
        recipesRecyclerView.setVisibility(View.VISIBLE);
    }
}