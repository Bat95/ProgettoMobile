package com.myrecipebook.myrecipebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.activities.GuidedStepsActivity;
import com.myrecipebook.myrecipebook.models.Recipe;
import com.myrecipebook.myrecipebook.utilities.Preferences;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetailFragment extends Fragment {

    private Recipe recipe;

    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recipe = getArguments().getParcelable("recipe");

        getActivity().setTitle(R.string.detail_recipe);

        TextView titleRecipeLabel = (TextView) view.findViewById(R.id.titoloRicettaDettaglio);
        TextView timeLabel = (TextView) view.findViewById(R.id.time_value);
        TextView doseLabel = (TextView) view.findViewById(R.id.dose_value);
        TextView categoryLabel = (TextView) view.findViewById(R.id.category_value);
        TextView ingredientList = (TextView) view.findViewById(R.id.ingredientlist);
        TextView stepsLabel = (TextView) view.findViewById(R.id.list_procedure);
        ImageView imageRecipe = (ImageView) view.findViewById(R.id.detail_image);
        final ImageView star_icon_favourite = (ImageView) view.findViewById(R.id.star_icon_preferences);

        Button procedure = (Button) view.findViewById(R.id.procedure_button);
        procedure.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), GuidedStepsActivity.class);
                i.putStringArrayListExtra("stepArray", (ArrayList<String>) recipe.steps);
                getActivity().startActivity(i);
            }
        });

        //recipe title
        titleRecipeLabel.setText(recipe.name);

        //difficulty
        switch (recipe.difficulty) {
            case 1 : view.findViewById(R.id.difficulty_icon_1).setVisibility(view.VISIBLE);
                break;
            case 2 : view.findViewById(R.id.difficulty_icon_1).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_2).setVisibility(view.VISIBLE);
                break;
            case 3 : view.findViewById(R.id.difficulty_icon_1).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_2).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_3).setVisibility(view.VISIBLE);
                break;
            case 4 : view.findViewById(R.id.difficulty_icon_1).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_2).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_3).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_4).setVisibility(view.VISIBLE);
                break;
            case 5 : view.findViewById(R.id.difficulty_icon_1).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_2).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_3).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_4).setVisibility(view.VISIBLE);
                view.findViewById(R.id.difficulty_icon_5).setVisibility(view.VISIBLE);
                break;
        }


        //time
        int hours = recipe.duration / 60;
        int minutes = recipe.duration % 60;
        if (hours <= 0) {
            timeLabel.setText(String.format(getContext().getString(R.string.minutes), minutes));
        }
        else {
            if (minutes == 0) {
                if (hours == 1) {
                    timeLabel.setText("1 ora");
                }
                else {
                    timeLabel.setText(String.format(getContext().getString(R.string.hours), hours));
                }
            }
            else {
                if (hours == 1) {
                    timeLabel.setText("1 ora e " + Integer.toString(minutes) + " minuti");
                } else {
                    timeLabel.setText(Integer.toString(hours) + " ore e " + Integer.toString(minutes) + " minuti");
                }
            }
        }

        // dose per person
        if (recipe.dosePerPerson == 1) doseLabel.setText(String.format(getContext().getString(R.string.dosePersona), recipe.dosePerPerson));
        else doseLabel.setText(String.format(getContext().getString(R.string.dosePersone), recipe.dosePerPerson));

        //category check
        switch (recipe.category) {

            case 1 : categoryLabel.setText(R.string.appetizer);
                break;
            case 2: categoryLabel.setText(R.string.first_dish);
                break;
            case 3 : categoryLabel.setText(R.string.second_dish);
                break;
            case 4 : categoryLabel.setText(R.string.dessert);
                break;
            case 5 : categoryLabel.setText(R.string.unique_dish);
                break;
        }

        for (String s : recipe.ingredients){
            ingredientList.setText(ingredientList.getText()+ "\u2022 " + s + "\n");
        }

        for (String item: recipe.steps) {
            stepsLabel.append(item);
            stepsLabel.append(" ");
        }

        //loading image
        Picasso.with(getContext())
                .load(recipe.mainPic)
                .placeholder(R.drawable.image_not_available)
                .into(imageRecipe);


        //add to favourite
        boolean isInFavorite = isFavoriteRecipe(recipe);
        star_icon_favourite.setTag(isInFavorite ? R.drawable.star_on : R.drawable.star_off);
        star_icon_favourite.setImageDrawable(ContextCompat.getDrawable(getContext(), isInFavorite ? R.drawable.star_on : R.drawable.star_off));

        star_icon_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) star_icon_favourite.getTag();
                if( tag == R.drawable.star_off ){
                    star_icon_favourite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_on));
                    star_icon_favourite.setTag(R.drawable.star_on);
                    addToFavorite(recipe);
                }else{
                    star_icon_favourite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.star_off));
                    star_icon_favourite.setTag(R.drawable.star_off);
                    removeFromFavorite(recipe);
                }
            }
        });
    }

    private boolean isFavoriteRecipe(Recipe recipe) {
        if(recipe == null || recipe.id <= 0) {
            return  false;
        }

        int[] favoriteRecipesIds = Preferences.get(getContext(), "favoriteRecipesIds", int[].class, new int[0]);
        for(int recipeId : favoriteRecipesIds) {
            if(recipe.id == recipeId) {
                return  true;
            }
        }

        return false;
    }

    private void removeFromFavorite(Recipe recipe) {
        if(!isFavoriteRecipe(recipe) || recipe.id <= 0) {
            return;
        }

        int[] favoriteRecipesIds = Preferences.get(getContext(), "favoriteRecipesIds", int[].class, new int[0]);
        ArrayList<Integer> favoriteIds = new ArrayList();
        for (int recipeId : favoriteRecipesIds) {
            if(recipeId == recipe.id) {
                continue;
            }

            favoriteIds.add(recipeId);
        }

        Preferences.store(getContext(), "favoriteRecipesIds", favoriteIds.toArray());

        Toast.makeText(getContext(), "Rimossa dai preferiti", Toast.LENGTH_SHORT).show();
    }

    private void addToFavorite(Recipe recipe) {
        if(isFavoriteRecipe(recipe) || recipe == null || recipe.id <= 0) {
            return;
        }

        int[] favoriteRecipesIds = Preferences.get(getContext(), "favoriteRecipesIds", int[].class, new int[0]);
        ArrayList<Integer> favoriteIds = new ArrayList();
        for (int recipeId : favoriteRecipesIds) {
            favoriteIds.add(recipeId);
        }

        favoriteIds.add(recipe.id);

        Preferences.store(getContext(), "favoriteRecipesIds", favoriteIds.toArray());

        Toast.makeText(getContext(), "Aggiunta ai preferiti", Toast.LENGTH_SHORT).show();
    }
}

