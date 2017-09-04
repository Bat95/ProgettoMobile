package com.myrecipebook.myrecipebook.adapters;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.fragments.RecipeDetailFragment;
import com.myrecipebook.myrecipebook.models.Recipe;
import com.squareup.picasso.Picasso;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private Context context;
    private FragmentManager mFragmentManager;
    private List<Recipe> recipelist;
    private Bitmap img;

    public ResultAdapter(Context context, ArrayList<Recipe> recipelist, FragmentManager fm){
        this.context = context;
        this.recipelist = recipelist;
        this.mFragmentManager = fm;

        Picasso.with(context).setIndicatorsEnabled(true);
    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_recipe, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Recipe currentRecipe = recipelist.get(position);

        // Bind image
        Picasso.with(context)
                .load(currentRecipe.mainPic)
                .placeholder(R.drawable.image_not_available)
                .into(holder.image);

        //recipe name
        holder.n.setText(currentRecipe.name);

        //time
        int hours = currentRecipe.duration / 60;
        int minutes = currentRecipe.duration % 60;
        if (hours <= 0)
            holder.time.setText(Integer.toString(minutes) + " minuti");
        else
            holder.time.setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + " ore");

        //difficulty
        switch (currentRecipe.difficulty) {
            case 1 : holder.icon_difficulty1.setVisibility(View.VISIBLE);
                break;
            case 2 : holder.icon_difficulty1.setVisibility(View.VISIBLE);
                holder.icon_difficulty2.setVisibility(View.VISIBLE);
                break;
            case 3 : holder.icon_difficulty1.setVisibility(View.VISIBLE);
                holder.icon_difficulty2.setVisibility(View.VISIBLE);
                holder.icon_difficulty3.setVisibility(View.VISIBLE);
                break;
            case 4 : holder.icon_difficulty1.setVisibility(View.VISIBLE);
                holder.icon_difficulty2.setVisibility(View.VISIBLE);
                holder.icon_difficulty3.setVisibility(View.VISIBLE);
                holder.icon_difficulty4.setVisibility(View.VISIBLE);
                break;
            case 5 : holder.icon_difficulty1.setVisibility(View.VISIBLE);
                holder.icon_difficulty2.setVisibility(View.VISIBLE);
                holder.icon_difficulty3.setVisibility(View.VISIBLE);
                holder.icon_difficulty4.setVisibility(View.VISIBLE);
                holder.icon_difficulty5.setVisibility(View.VISIBLE);
                break;
        }

        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                img = ((BitmapDrawable)holder.image.getDrawable()).getBitmap();;
                Fragment f = new RecipeDetailFragment(recipelist.get(position),img);
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.replace(R.id.content_main, f);
                ft.addToBackStack("results");
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipelist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ImageView image;
        public TextView n, time;
        public CoordinatorLayout layout;
        public ImageView icon_difficulty1;
        public ImageView icon_difficulty2;
        public ImageView icon_difficulty3;
        public ImageView icon_difficulty4;
        public ImageView icon_difficulty5;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.recipe_image);
            image.setImageResource(R.drawable.image_not_available);
            n = (TextView) v.findViewById(R.id.recipe_name);
            time = (TextView) v.findViewById(R.id.recipe_time_value);
            layout = (CoordinatorLayout) v.findViewById(R.id.recipe_layout);
            icon_difficulty1 = (ImageView) v.findViewById(R.id.difficulty_icon_1);
            icon_difficulty2 = (ImageView) v.findViewById(R.id.difficulty_icon_2);
            icon_difficulty3 = (ImageView) v.findViewById(R.id.difficulty_icon_3);
            icon_difficulty4 = (ImageView) v.findViewById(R.id.difficulty_icon_4);
            icon_difficulty5 = (ImageView) v.findViewById(R.id.difficulty_icon_5);
        }

    }
    
}
