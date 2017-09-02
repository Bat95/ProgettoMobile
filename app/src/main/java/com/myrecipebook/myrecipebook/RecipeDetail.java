package com.myrecipebook.myrecipebook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myrecipebook.myrecipebook.utilities.Preferences;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Sonia on 23/08/17.
 */

public class RecipeDetail extends Fragment {

    private Recipe recipe;
    private Bitmap recipeimg;

    public RecipeDetail() {}

    public RecipeDetail(Recipe recipe, Bitmap recipeimg){
        this.recipe = recipe;
        this.recipeimg = recipeimg;}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                Intent i = new Intent(getActivity(), GuidedSteps.class);
                i.putStringArrayListExtra("stepArray", (ArrayList<String>) recipe.steps);
                getActivity().startActivity(i);
            }
        });
        //ListView listIngred = (ListView) view.findViewById(R.id.list_ingredients);

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
        if (hours <= 0) timeLabel.setText(Integer.toString(minutes) + " minuti");
        else timeLabel.setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + " ore");

        // dose per person
        if (recipe.dosePerPerson == 1) doseLabel.setText(Integer.toString(recipe.dosePerPerson) + " persona");
        else doseLabel.setText(Integer.toString(recipe.dosePerPerson) + " persone");

        //category check
        switch (recipe.category) {

            case 1 : categoryLabel.setText("Antipasti");
                break;
            case 2: categoryLabel.setText("Primi piatti");
                break;
            case 3 : categoryLabel.setText("Secondi piatti");
                break;
            case 4 : categoryLabel.setText("Dolci");
                break;
            case 5 : categoryLabel.setText("Piatto Unico");
                break;
        }

        //listIngred.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recipe.ingredients));
        for (String s : recipe.ingredients){
            ingredientList.setText(ingredientList.getText()+ "- " + s + "\n");
        }

        for (String item: recipe.steps) {
            stepsLabel.append(item);
            stepsLabel.append(" ");
        }

        //loading image
        imageRecipe.setImageBitmap(recipeimg);
        new DownloadImageTask(imageRecipe).execute(recipe.mainPic);


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
        if(!isFavoriteRecipe(recipe) || recipe == null || recipe.id <= 0) {
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

