package com.myrecipebook.myrecipebook;

/**
 * Created by Sonia on 12/05/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    class InfoDto {
        public String origin;
    }

    Button search_button;

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
        final InfoDto[] infoRecipe = {new InfoDto()};

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



        Spinner needed_time = (Spinner) view.findViewById(R.id.spinner);

        // http post and get request on search button clicked
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Prendo i valori
                boolean isApetizer = appetizers.isEnabled();
                boolean isFDish = first_dishes.isEnabled();
                boolean isSDish = second_dishes.isEnabled();
                boolean isDessert = desserts.isEnabled();
                boolean intolerances = apply_intolerances.isEnabled();
                boolean storeroom = apply_storeroom.isEnabled();

                int selectedTime = 0;
                switch (timesp.getSelectedItem().toString()){
                    case "nessun limite":
                        selectedTime = 0;
                    case "meno di 30 minuti":
                        selectedTime = 1;
                    case "meno di 1 ora":
                        selectedTime = 2;
                    case "meno di 2 ore":
                        selectedTime = 3;
                    case "meno di 3 ore":
                        selectedTime = 4;
                    case "oltre 3 ore":
                        selectedTime = 5;
                }

                int selectedDifficulty = 0;
                switch (difficultysp.getSelectedItem().toString()){
                    case "tutte":
                        selectedDifficulty = 0;
                    case "1":
                        selectedDifficulty = 1;
                    case "2":
                        selectedDifficulty = 2;
                    case "3":
                        selectedDifficulty = 3;
                }

                InfoDto info = new InfoDto();
                info.origin = "Inviato";

                HttpHelper.Post(
                        getActivity().getApplicationContext(),
                        "http://192.168.43.155:5000/api/values/info",
                        info,
                        new BaseHttpResponseHandler<InfoDto>(InfoDto.class) {

                            @Override
                            public void handleResponse(InfoDto response) {
                                infoRecipe[0] = response;
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
