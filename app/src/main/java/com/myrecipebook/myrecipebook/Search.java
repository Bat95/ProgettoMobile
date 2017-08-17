package com.myrecipebook.myrecipebook;

/**
 * Created by Sonia
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Search extends Fragment {

    // Lista fittizia della dispensa e degli ingredienti
    List<String> pantry;
    List<String> ingredSelected;

    Button search_button;
    List<Recipe> resultedRecipes;

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

        final TextView recipe_name_input = (TextView) view.findViewById(R.id.recipe_name_input);

        final ArrayList<String> available_ingredients = new ArrayList<String>();
        for (String s : ingredientsList){
            available_ingredients.add(s);
        }

        //Creo un RecyclerView
        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // autocomplete for the ingredients
        final AutoCompleteTextView input_ingredients = (AutoCompleteTextView) view.findViewById(R.id.input_ingredients);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, available_ingredients);
        input_ingredients.setAdapter(adapter);
        final ArrayList<String> ingredientArray = new ArrayList<>();
        input_ingredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                input_ingredients.setText("");
                input_ingredients.clearFocus();
                Object o = adapterView.getItemAtPosition(i);
                String ing = o.toString();
                ingredientArray.add(ing);
                adapter.remove(ing);
                adapter.notifyDataSetChanged();
            }
        });


        final RecyclerView.Adapter mAdapter = new IngredientAdapter(ingredientArray);
        mRecyclerView.setAdapter(mAdapter);

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
                HashSet<String> ingredSet = new HashSet<>();

                ingredSet.addAll(ingredSelected);

                //Prendo i valori
                filterInfo.isApetizer = appetizers.isEnabled();
                filterInfo.isFDish = first_dishes.isEnabled();
                filterInfo.isSDish = second_dishes.isEnabled();
                filterInfo.isDessert = desserts.isEnabled();
                filterInfo.intolerances = apply_intolerances.isEnabled();

                if(apply_storeroom.isEnabled()) {
                    ingredSet.addAll(pantry);
                }

                switch (timesp.getSelectedItem().toString()){
                    case "nessun limite":
                        filterInfo.selectedTime = 0;
                    case "meno di 30 minuti":
                        filterInfo.selectedTime = 30;
                    case "meno di 1 ora":
                        filterInfo.selectedTime = 60;
                    case "meno di 2 ore":
                        filterInfo.selectedTime = 120;
                    case "meno di 3 ore":
                        filterInfo.selectedTime = 180;
                    case "oltre 3 ore":
                        filterInfo.selectedTime = 300;
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
                    case "4":
                        filterInfo.selectedDifficulty = 4;
                    case "5":
                        filterInfo.selectedDifficulty = 5;
                }

                if (!TextUtils.isEmpty(recipe_name_input.getText())) {
                    filterInfo.recipeName = recipe_name_input.getText().toString();
                }

                filterInfo.ingredientsList = new ArrayList<>(ingredSet);

                HttpHelper.Post(
                        getActivity().getApplicationContext(),
                        "http://646ffb06.ngrok.io/api/values/filterInfo",
                        filterInfo,
                        new BaseHttpResponseHandler<InfoDto>(InfoDto.class) {

                            @Override
                            public void handleResponse(InfoDto response) {
                                resultedRecipes = new ArrayList<>(response.resultRecipes);
                            }

                            @Override
                            public void handleError(String errorMessage) {
                                super.handleError(errorMessage);
                            }
                        });
            }
        });

    }

}
