package com.myrecipebook.myrecipebook;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;


/**
 * Created by Sonia on 23/08/17.
 */

public class RecipeDetail extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.detail_recipe);

        Recipe detailRecipe = new Recipe();

        // fake info
        detailRecipe.Name = "Pizza";
        detailRecipe.Category = 5;
        detailRecipe.Difficulty = 3;
        detailRecipe.Duration = 330;
        detailRecipe.DosePerPerson = 2;
        detailRecipe.Ingredients = new String[]{"100g di pasta per pizza", "30g salsa di pomodoro"};
        detailRecipe.MainPic = "https://upload.wikimedia.org/wikipedia/commons/a/a3/Eq_it-na_pizza-margherita_sep2005_sml.jpg";
        detailRecipe.Steps = new String[] {"fai passo1.", "fai passo 2.", "fai passo3.", "finisci.", "cucina.", "mangia."};

        TextView titleRecipeLabel = (TextView) view.findViewById(R.id.titoloRicettaDettaglio);
        TextView difficultyLabel = (TextView) view.findViewById(R.id.difficulty_value);
        TextView timeLabel = (TextView) view.findViewById(R.id.time_value);
        TextView doseLabel = (TextView) view.findViewById(R.id.dose_value);
        TextView categoryLabel = (TextView) view.findViewById(R.id.category_value);
        TextView stepsLabel = (TextView) view.findViewById(R.id.list_procedure);
        ImageView imageRecipe = (ImageView) view.findViewById(R.id.detail_image);

        ListView listIngred = (ListView) view.findViewById(R.id.list_ingredients);

        //recipe title
        titleRecipeLabel.setText(detailRecipe.Name);

        //difficulty
        difficultyLabel.setText(Integer.toString(detailRecipe.Difficulty));

        //time
        int hours = detailRecipe.Duration / 60;
        int minutes = detailRecipe.Duration % 60;
        if (hours <= 0) timeLabel.setText(Integer.toString(minutes) + " minuti");
        else timeLabel.setText(Integer.toString(hours) + ":" + Integer.toString(minutes) + " ore");

        // dose per person
        if (detailRecipe.DosePerPerson == 1) doseLabel.setText(Integer.toString(detailRecipe.DosePerPerson) + " persona");
        else doseLabel.setText(Integer.toString(detailRecipe.DosePerPerson) + " persone");

        //category check
        switch (detailRecipe.Category) {
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

        listIngred.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, detailRecipe.Ingredients));

        for (String item: detailRecipe.Steps) {
            stepsLabel.append(item);
            stepsLabel.append(" ");
        }

        //loading image
        new DownloadImageTask(imageRecipe).execute(detailRecipe.MainPic);

    }
}

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}