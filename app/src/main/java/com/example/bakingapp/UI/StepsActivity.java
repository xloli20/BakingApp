package com.example.bakingapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.bakingapp.Adapters.StepsAdapter;
import com.example.bakingapp.Models.Recipes;
import com.example.bakingapp.Models.RecipesIngredients;
import com.example.bakingapp.Models.RecipesSteps;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Models.DummyContent;
import com.example.bakingapp.R;
import com.example.bakingapp.Utils.JsonUtils;
import com.example.bakingapp.Utils.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepsActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListener {
    private static final String TAG = StepsActivity.class.getSimpleName() ;

    ArrayList<RecipesIngredients> ingredients;

    ArrayList<RecipesSteps> steps;
    RecyclerView recyclerView;
    Recipes recipes;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final Intent intent = getIntent();
        recipes = intent.getParcelableExtra("recipes");
        Log.d(TAG, "onCreate: recipes "+recipes.getrId());

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        ((RecyclerView) recyclerView).setLayoutManager(linearLayoutManager);

        URL url = NetworkUtil.buildUrl();
        new StepsQueryTask().execute(url);

        setStepsAdapter();
    }

    private void setStepsAdapter() {
        Log.d(TAG, "setStepsAdapter: " +steps);
        StepsAdapter stepsAdapter = new StepsAdapter(steps,this);
        ((RecyclerView) recyclerView).setHasFixedSize(true);
        ((RecyclerView) recyclerView).setAdapter(stepsAdapter);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

    @Override
    public void onListClickItem(int clickedItemIndex) {

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepsActivity mParentActivity;//
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;//

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.id);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(StepsActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.steps_items, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
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
                ((RecyclerView) recyclerView).setVisibility(View.VISIBLE);

                try {
                    Log.d(TAG, "onPostExecute: steps "+steps);
                    Log.d(TAG, "onPostExecute: steps "+s);
                    Log.d(TAG, "onPostExecute: steps "+recipes);

                    steps = JsonUtils.parseStepsJson(s,recipes.getrId());
                    setStepsAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
            }
        }
    }
}
