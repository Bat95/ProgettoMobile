package com.myrecipebook.myrecipebook.adapters;

import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.myrecipebook.myrecipebook.R;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<String> ingredients;
    private ArrayAdapter<String> adapter;

    public IngredientAdapter(List<String> ingredients, ArrayAdapter<String> adapter){
        this.ingredients = ingredients;
        this.adapter = adapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_ingredients, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt.setText(ingredients.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int newPosition = holder.getAdapterPosition();
                adapter.add(ingredients.get(position));
                ingredients.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, ingredients.size());
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txt;
        public ImageView delete;

        public ViewHolder(View v) {
            super(v);
            txt = (TextView) v.findViewById(R.id.chiptext);
            delete = (ImageView) v.findViewById(R.id.delete);
        }


    }

}