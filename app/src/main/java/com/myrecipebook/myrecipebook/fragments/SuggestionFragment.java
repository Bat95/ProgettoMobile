package com.myrecipebook.myrecipebook.fragments;

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
import com.myrecipebook.myrecipebook.utilities.BaseHttpResponseHandler;
import com.myrecipebook.myrecipebook.utilities.HttpHelper;
import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.models.Recipe;
import com.myrecipebook.myrecipebook.adapters.ResultAdapter;
import java.util.ArrayList;
import java.util.Arrays;


public class SuggestionFragment extends Fragment {

    private View containerView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mGrid;
    private ArrayList<Recipe> suggestedRecipes;
    private TextView txtNoSuggestion;
    private TextView txtLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryBackground));

        if(containerView == null) {
            containerView = inflater.inflate(R.layout.suggestion, container, false);
        }

        return containerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ricette suggerite");

        txtNoSuggestion = (TextView) view.findViewById(R.id.txtNoSuggestion);
        txtLoading = (TextView) view.findViewById(R.id.txtLoading);

        boolean refreshRecipes = suggestedRecipes == null;
        if(refreshRecipes) {
            suggestedRecipes = new ArrayList<>();
            txtLoading.setVisibility(View.VISIBLE);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerSuggested);
        mGrid = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGrid);

        mAdapter = new ResultAdapter(getContext(), suggestedRecipes, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        if(refreshRecipes) {
            getSuggestedRecipes();
        }
    }

    private void getSuggestedRecipes() {

        try {

            HttpHelper.Get(
                    getActivity().getApplicationContext(),
                    getString(R.string.serverIp) + getString(R.string.suggested_API),
                    new BaseHttpResponseHandler<Recipe[]>(Recipe[].class) {

                        @Override
                        public void handleResponse(Recipe[] response) {
                            txtLoading.setVisibility(View.GONE);
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
                            txtLoading.setVisibility(View.GONE);
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
