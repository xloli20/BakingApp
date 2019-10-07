package com.example.bakingapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.Models.RecipesSteps;
import com.example.bakingapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private ArrayList<RecipesSteps> steps;

    public StepsAdapter(ArrayList<RecipesSteps> steps, ListItemClickListener listItemClickListener) {
        this.steps = steps;
        this.listItemClickListener = listItemClickListener;
    }

    private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListClickItem(int clickedItemIndex);
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_items, parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView descriptionTextView;

        StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.id_text);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            descriptionTextView.setText(steps.get(position).getsDescription());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListClickItem(clickedPosition);
        }
    }
}
