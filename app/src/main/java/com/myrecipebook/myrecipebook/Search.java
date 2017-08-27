package com.myrecipebook.myrecipebook;

/**
 * Created by Sonia
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static android.util.Log.*;


public class Search extends Fragment implements Serializable {

    // Lista fittizia della dispensa e degli ingredienti
    List<String> pantry;
    List<String> intolerancesList;

    List<String> ingredSelected;

    Button search_button;

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

        final ArrayList<String> available_ingredients = new ArrayList<>();
        for (String s : ingredientsList){
            available_ingredients.add(s);
        }

        pantry = new ArrayList<>();
        ingredSelected = new ArrayList<>();
        intolerancesList = new ArrayList<>();
        pantry.add("ingrediente1");
        pantry.add("ingrediente2");
        ingredSelected.add("ingrediente3");
        ingredSelected.add("ingrediente4");

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


        final RecyclerView.Adapter mAdapter = new IngredientAdapter(ingredientArray,adapter);
        mRecyclerView.setAdapter(mAdapter);

        //Variabili delle checkbox
        final CheckBox apply_intolerances = (CheckBox) view.findViewById(R.id.apply_intolerances);
        final CheckBox apply_storeroom = (CheckBox) view.findViewById(R.id.apply_storeroom);
        final CheckBox appetizers = (CheckBox) view.findViewById(R.id.appetizers);
        final CheckBox first_dishes = (CheckBox) view.findViewById(R.id.first_dishes);
        final CheckBox second_dishes = (CheckBox) view.findViewById(R.id.second_dishes);
        final CheckBox desserts = (CheckBox) view.findViewById(R.id.desserts);
        final CheckBox unique = (CheckBox) view.findViewById(R.id.unique);
        final Spinner timesp = (Spinner) view.findViewById(R.id.timespinner);
        final Spinner difficultysp = (Spinner) view.findViewById(R.id.difficultyspinner);

        // http post and get request on search button clicked
        search_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                InfoDto filterInfo = new InfoDto();
                HashSet<String> ingredSet = new HashSet<>();

                ingredSet.addAll(ingredientArray);



                //Prendo i valori
                filterInfo.isApetizer = appetizers.isChecked();
                filterInfo.isFDish = first_dishes.isChecked();
                filterInfo.isSDish = second_dishes.isChecked();
                filterInfo.isDessert = desserts.isChecked();
                filterInfo.intolerances = apply_intolerances.isChecked();
                filterInfo.isUnique = unique.isChecked();

                filterInfo.intolerance = intolerancesList;

                if(apply_storeroom.isChecked()) {
                    ingredSet.addAll(pantry);
                }

                switch (timesp.getSelectedItem().toString()){
                    case "nessun limite":
                        filterInfo.selectedTime = 0;
                        break;
                    case "meno di 30 minuti":
                        filterInfo.selectedTime = 30;
                        break;
                    case "meno di 1 ora":
                        filterInfo.selectedTime = 60;
                        break;
                    case "meno di 2 ore":
                        filterInfo.selectedTime = 120;
                        break;
                    case "meno di 3 ore":
                        filterInfo.selectedTime = 180;
                        break;
                    case "oltre 3 ore":
                        filterInfo.selectedTime = 300;
                        break;
                }

                switch (difficultysp.getSelectedItem().toString()){
                    case "tutte":
                        filterInfo.selectedDifficulty = 0;
                        break;
                    case "1":
                        filterInfo.selectedDifficulty = 1;
                        break;
                    case "2":
                        filterInfo.selectedDifficulty = 2;
                        break;
                    case "3":
                        filterInfo.selectedDifficulty = 3;
                        break;
                    case "4":
                        filterInfo.selectedDifficulty = 4;
                        break;
                    case "5":
                        filterInfo.selectedDifficulty = 5;
                        break;
                }

                if (!TextUtils.isEmpty(recipe_name_input.getText())) {
                    filterInfo.recipeName = recipe_name_input.getText().toString();
                }

                filterInfo.ingredientsList = new ArrayList<>(ingredSet);

                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Ricerca ricette in corso...");
                pd.show();

                try {
                    HttpHelper.Post(
                            getActivity().getApplicationContext(),
                            "http://2cb8f52d.ngrok.io/api/values/filterInfo",
                            filterInfo,
                            new BaseHttpResponseHandler<RecipesFilterResult>(RecipesFilterResult.class) {

                                @Override
                                public void handleResponse(RecipesFilterResult response) {
                                    pd.dismiss();
                                    //Replace fragment passing Recipe List
                                    presentResultsRecipesFragment(response.recipes);
                                }

                                @Override
                                public void handleError(String errorMessage) {
                                    pd.dismiss();
                                    super.handleError(errorMessage);
                                }
                            });
                }
                catch (Exception e) {
                    pd.dismiss();
                }

            }
        });

    }

    void presentResultsRecipesFragment(List<Recipe> recipes) {
        Bundle b = new Bundle();
        b.putParcelableArrayList("recipelist", (ArrayList<Recipe>) recipes);
        Fragment resultFragment = new Results();
        resultFragment.setArguments(b);
        FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.content_main, resultFragment);
        trans.addToBackStack("search");
        trans.commit();
    }
}
