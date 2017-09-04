package com.myrecipebook.myrecipebook.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.models.Recipe;
import com.myrecipebook.myrecipebook.adapters.ResultAdapter;

import java.util.ArrayList;

public class ResultsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGrid;

    ArrayList<Recipe> resultedRecipes ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resultedRecipes  = getArguments().getParcelableArrayList(getString(R.string.recipelist_Bkey));
        int resource =  R.layout.results;
        if (resultedRecipes != null) {
            if (resultedRecipes.size() == 0) {
                resource = R.layout.noresults;
            }
        }
        container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryBackground));
        return inflater.inflate(resource,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.results_title));


        if(resultedRecipes.size()>0) {
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewResults);
            mGrid = new GridLayoutManager(getContext(), 2);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mGrid);

            final RecyclerView.Adapter mAdapter = new ResultAdapter(getContext(), resultedRecipes, getFragmentManager());
            mRecyclerView.setAdapter(mAdapter);
        }

    }

}
