package com.myrecipebook.myrecipebook;

/**
 * Created by Sonia
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class Search extends Fragment {


    class  InfoDto {

        boolean isApetizer;
        boolean isFDish;
        boolean isSDish;
        boolean isDessert;
        boolean intolerances;
        boolean storeroom;

        int selectedTime = 0;

        int selectedDifficulty = 0;

        Recipe resultRecipes;
    }

    class Recipe {
        public String Name;
        public String MainPic;
        public int DosePerPerson;
        public String[] Steps;
        public String[] StepImages;
        public String[] Ingredients;
        public int Difficulty;
        public int Duration;
        public int Category;
        public String[] Tag;
    }

    Button search_button;
    Recipe resultedRecipes;

    // lista degli ingredienti tra cui un utente può scegliere
    private static final String[] ingredientsList = new String[] {
            "Latte", "Uova", "Pomodori", "Zucchero", "una lista di tutti gli ingredienti"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.recipe_search_title);

        // initialize variables
        search_button = (Button) view.findViewById(R.id.search_button);
        resultedRecipes = new Recipe();

        final TextView recipe_name_input = (TextView) view.findViewById(R.id.recipe_name_input);

        // autocomplete for the ingredients
        AutoCompleteTextView input_ingredients = (AutoCompleteTextView) view.findViewById(R.id.input_ingredients);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, ingredientsList);
        input_ingredients.setAdapter(adapter);

        //Variabili delle checkbox
        final TextView apply_intolerances = (TextView) view.findViewById(R.id.apply_intolerances);
        final TextView apply_storeroom = (TextView) view.findViewById(R.id.apply_storeroom);
        final TextView appetizers = (TextView) view.findViewById(R.id.appetizers);
        final TextView first_dishes = (TextView) view.findViewById(R.id.first_dishes);
        final TextView second_dishes = (TextView) view.findViewById(R.id.second_dishes);
        final TextView desserts = (TextView) view.findViewById(R.id.desserts);
        final Spinner timesp = (Spinner) view.findViewById(R.id.timespinner);
        final Spinner difficultysp = (Spinner) view.findViewById(R.id.difficultyspinner);



        // http post and get request on search button clicked
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InfoDto filterInfo = new InfoDto();

                //Prendo i valori
                filterInfo.isApetizer = appetizers.isEnabled();
                filterInfo.isFDish = first_dishes.isEnabled();
                filterInfo.isSDish = second_dishes.isEnabled();
                filterInfo.isDessert = desserts.isEnabled();
                filterInfo.intolerances = apply_intolerances.isEnabled();
                filterInfo.storeroom = apply_storeroom.isEnabled();

                switch (timesp.getSelectedItem().toString()){
                    case "nessun limite":
                        filterInfo.selectedTime = 0;
                    case "meno di 30 minuti":
                        filterInfo.selectedTime = 1;
                    case "meno di 1 ora":
                        filterInfo.selectedTime = 2;
                    case "meno di 2 ore":
                        filterInfo.selectedTime = 3;
                    case "meno di 3 ore":
                        filterInfo.selectedTime = 4;
                    case "oltre 3 ore":
                        filterInfo.selectedTime = 5;
                }

                switch (difficultysp.getSelectedItem().toString()){
                    case "tutte":
                        filterInfo.selectedDifficulty = 0;
                    case "1":
                        filterInfo.selectedDifficulty = 1;
                    case "2":
                        filterInfo.selectedDifficulty = 2;
                    case "3":
                        filterInfo.selectedDifficulty = 3;
                }

                HttpHelper.Post(
                        getActivity().getApplicationContext(),
                        "http://646ffb06.ngrok.io/api/values/filterInfo",
                        filterInfo,
                        new BaseHttpResponseHandler<InfoDto>(InfoDto.class) {

                            @Override
                            public void handleResponse(InfoDto response) {
                                resultedRecipes = response.resultRecipes;
                            }

                            @Override
                            public void handleError(String errorMessage) {
                                super.handleError(errorMessage);
                            }
                        });

                /*HttpHelper.Get(
                        getActivity().getApplicationContext(),
                        "http://192.168.43.155:5000/api/values/5",
                        new BaseHttpResponseHandler<String>(String.class) {

                            @Override
                            public void handleResponse(String response) {
                                infoRecipe.origin = response;
                            }

                            @Override
                            public void handleError(String errorMessage) {
                                super.handleError(errorMessage);
                            }
                        }
                );*/

            }
        });

    }
}
