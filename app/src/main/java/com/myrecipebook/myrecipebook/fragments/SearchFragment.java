package com.myrecipebook.myrecipebook.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.myrecipebook.myrecipebook.utilities.BaseHttpResponseHandler;
import com.myrecipebook.myrecipebook.utilities.HttpHelper;
import com.myrecipebook.myrecipebook.models.RecipeFilter;
import com.myrecipebook.myrecipebook.adapters.IngredientAdapter;
import com.myrecipebook.myrecipebook.data.IngredientsStore;
import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.models.Recipe;
import com.myrecipebook.myrecipebook.models.RecipesFilterResult;
import com.myrecipebook.myrecipebook.utilities.Preferences;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class SearchFragment extends Fragment implements Serializable {

    List<String> intolerancesList;

    final ArrayList<String> ingredientArray = new ArrayList<>();

    FloatingActionButton search_button;

    IngredientsStore ingredients = new IngredientsStore();

    private ArrayList<String> ingredientsList;


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
        search_button = (FloatingActionButton) view.findViewById(R.id.search_button);

        ingredientsList = new ArrayList<>();
        ingredientsList.addAll(Arrays.asList(ingredients.ingredients));

        final TextView recipe_name_input = (TextView) view.findViewById(R.id.recipe_name_input);

        final ArrayList<String> available_ingredients = new ArrayList<>();
        for (String s : ingredientsList){
            available_ingredients.add(s);
        }

        intolerancesList = new ArrayList<>();

        //Creo un RecyclerView
        final RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // autocomplete for the ingredients
        final AutoCompleteTextView input_ingredients = (AutoCompleteTextView) view.findViewById(R.id.input_ingredients);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, available_ingredients);
        input_ingredients.setAdapter(adapter);

        final RecyclerView.Adapter mAdapter = new IngredientAdapter(ingredientArray, adapter);
        mRecyclerView.setAdapter(mAdapter);

        input_ingredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                input_ingredients.setText("");
                Object o = adapterView.getItemAtPosition(i);
                String ing = o.toString();
                ingredientArray.add(ing);
                adapter.remove(ing);
                adapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
            }
        });

        //Variabili delle checkbox
        final CheckBox apply_intolerances = (CheckBox) view.findViewById(R.id.apply_intolerances);
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

                RecipeFilter filterInfo = new RecipeFilter();
                HashSet<String> ingredSet = new HashSet<>();

                ingredSet.addAll(ingredientArray);



                //Prendo i valori
                filterInfo.isApetizer = appetizers.isChecked();
                filterInfo.isFDish = first_dishes.isChecked();
                filterInfo.isSDish = second_dishes.isChecked();
                filterInfo.isDessert = desserts.isChecked();
                filterInfo.intolerances = apply_intolerances.isChecked();
                filterInfo.isUnique = unique.isChecked();

                // AllergiesFragment
                if (Preferences.get(getContext(), "latticini", boolean.class, false))
                {
                    intolerancesList.add("latticini");
                }
                if (Preferences.get(getContext(), "frutta", boolean.class, false))
                {
                    intolerancesList.add("frutta a guscio");
                }
                if (Preferences.get(getContext(), "crostacei", boolean.class, false))
                {
                    intolerancesList.add("crostacei");
                }
                if (Preferences.get(getContext(), "uova", boolean.class, false))
                {
                    intolerancesList.add("uova");
                }
                if (Preferences.get(getContext(), "soia", boolean.class, false))
                {
                    intolerancesList.add("soia");
                }
                if (Preferences.get(getContext(), "frumento", boolean.class, false))
                {
                    intolerancesList.add("frumento e glutine");
                }
                if (Preferences.get(getContext(), "pesce", boolean.class, false))
                {
                    intolerancesList.add("pesce");
                }
                filterInfo.intolerance = intolerancesList;

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
                pd.setCancelable(false);
                pd.show();

                try {
                    HttpHelper.Post(
                            getActivity().getApplicationContext(),
                            getString(R.string.serverIp)+"filterInfo",
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
                    Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    void presentResultsRecipesFragment(List<Recipe> recipes) {
        Bundle b = new Bundle();
        b.putParcelableArrayList("recipelist", (ArrayList<Recipe>) recipes);
        Fragment resultFragment = new ResultsFragment();
        resultFragment.setArguments(b);
        FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.content_main, resultFragment);
        trans.addToBackStack("search");
        trans.commit();
    }
}
