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
        final InfoDto infoRecipe = new InfoDto();

        // autocomplete for the ingredients
        AutoCompleteTextView input_ingredients = (AutoCompleteTextView) view.findViewById(R.id.input_ingredients);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, ingredientsList);
        input_ingredients.setAdapter(adapter);

        // http get request on search button clicked
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HttpHelper.Get(
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
                );

            }
        });

    }
}
