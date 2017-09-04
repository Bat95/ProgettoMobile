package com.myrecipebook.myrecipebook;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Results extends Fragment {

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

            final RecyclerView.Adapter mAdapter = new ResultAdapter(resultedRecipes, getFragmentManager());
            mRecyclerView.setAdapter(mAdapter);
        }

    }

}
