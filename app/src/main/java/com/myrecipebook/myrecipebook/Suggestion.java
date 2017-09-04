package com.myrecipebook.myrecipebook;

/**
 * Created by Sonia on 12/05/17.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.TextView;

import com.myrecipebook.myrecipebook.utilities.Preferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Suggestion extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mGrid;
    private ArrayList<Recipe> suggestedRecipes;
    private TextView txtNoSuggestion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        suggestedRecipes = new ArrayList<>();

        container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryBackground));
        return inflater.inflate(R.layout.suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ricette suggerite");

        txtNoSuggestion = (TextView) view.findViewById(R.id.txtNoSuggestion);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerSuggested);
        mGrid = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGrid);

        mAdapter = new ResultAdapter(suggestedRecipes, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        getSuggestedRecipes();


        }

    private void getSuggestedRecipes() {

        try {

            HttpHelper.Get(
                    getActivity().getApplicationContext(),
                    getString(R.string.serverIp) + getString(R.string.suggested_API),
                    new BaseHttpResponseHandler<Recipe[]>(Recipe[].class) {

                        @Override
                        public void handleResponse(Recipe[] response) {
                            suggestedRecipes.clear();

                            if (response != null) {
                                suggestedRecipes.addAll(Arrays.asList(response));
                            }

                            mAdapter.notifyDataSetChanged();
                            handleNoResults();
                        }

                        @Override
                        public void handleError(String errorMessage) {
                            super.handleError(errorMessage);
                            handleNoResults();
                        }
                    });
        } catch (Exception e) {

            handleNoResults();
        }
    }

    private void handleNoResults() {
        boolean hasSuggestion = suggestedRecipes != null && suggestedRecipes.size() > 0;
        txtNoSuggestion.setVisibility(hasSuggestion ? View.GONE : View.VISIBLE);
    }

}
