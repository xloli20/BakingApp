package com.example.bakingapp.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Models.Recipes;
import com.example.bakingapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

    private static final String TAG = RecipesAdapter.class.getSimpleName();

    private ArrayList<Recipes> recipes;

    public RecipesAdapter(ArrayList<Recipes> recipes, ListItemClickListener listItemClickListener) {
        this.recipes = recipes;
        this.listItemClickListener = listItemClickListener;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_items, parent, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    final private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListClickItem(int clickedItemIndex);
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipesTextView;

        RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            recipesTextView = itemView.findViewById(R.id.recipes_name);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Log.d(TAG, "bind: " + recipes.get(position).getrName());
            recipesTextView.setText(recipes.get(position).getrName());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListClickItem(clickedPosition);
        }
    }
}
