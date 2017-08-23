package com.myrecipebook.myrecipebook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.detail_recipe);

        Recipe detailRecipe = new Recipe();

        TextView difficultyLabel = (TextView) view.findViewById(R.id.difficulty_value);
        TextView timeLabel = (TextView) view.findViewById(R.id.time_value);
        TextView doseLabel = (TextView) view.findViewById(R.id.dose_value);
        TextView categoryLabel = (TextView) view.findViewById(R.id.category_value);
        TextView stepsLabel = (TextView) view.findViewById(R.id.list_procedure);
        ImageView imageRecipe = (ImageView) view.findViewById(R.id.detail_image);

        ListView listIngred = (ListView) view.findViewById(R.id.list_ingredients);

        difficultyLabel.setText(detailRecipe.Difficulty);
        timeLabel.setText(detailRecipe.Duration);
        doseLabel.setText(detailRecipe.DosePerPerson);
        categoryLabel.setText(detailRecipe.Category);

        listIngred.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, detailRecipe.Ingredients));

        for (String item: detailRecipe.Steps) {
            stepsLabel.append(item);
            stepsLabel.append(" ");
        }

        new DownloadImageTask(imageRecipe).execute(detailRecipe.MainPic);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_details, container, false);
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
