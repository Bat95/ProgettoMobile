package com.myrecipebook.myrecipebook.fragments;

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
import com.myrecipebook.myrecipebook.utilities.BaseHttpResponseHandler;
import com.myrecipebook.myrecipebook.utilities.HttpHelper;
import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.models.Recipe;
import com.myrecipebook.myrecipebook.adapters.ResultAdapter;
import com.myrecipebook.myrecipebook.utilities.Preferences;
import java.util.ArrayList;
import java.util.Arrays;


public class FavouriteFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mGrid;
    private ArrayList<Recipe> favouriteRecipes;
    private TextView txtNoFavorite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        favouriteRecipes = new ArrayList<>();
        container.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.primaryBackground));
        return inflater.inflate(R.layout.favourite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Preferiti");

        txtNoFavorite = (TextView) view.findViewById(R.id.txtNoFavorite);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerFavourite);
        mGrid = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGrid);

        mAdapter = new ResultAdapter(getContext(), favouriteRecipes, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        getFavouriteRecipes();
    }

    private void getFavouriteRecipes() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage(getString(R.string.loading_message));
        pd.setCancelable(false);
        pd.show();

        try {
            int[] favoriteRecipesIds = Preferences.get(getContext(), "favoriteRecipesIds", int[].class, new int[0]);

            HttpHelper.Post(
                    getActivity().getApplicationContext(),
                    getString(R.string.serverIp) + "favorite",
                    favoriteRecipesIds,
                    new BaseHttpResponseHandler<Recipe[]>(Recipe[].class) {

                        @Override
                        public void handleResponse(Recipe[] response) {
                            favouriteRecipes.clear();

                            if (response != null) {
                                favouriteRecipes.addAll(Arrays.asList(response));
                            }

                            mAdapter.notifyDataSetChanged();
                            handleNoResults();
                            pd.dismiss();
                        }

                        @Override
                        public void handleError(String errorMessage) {
                            super.handleError(errorMessage);
                            handleNoResults();
                            pd.dismiss();
                        }
                    });
        } catch (Exception e) {
            if(pd.isShowing()) {
                pd.dismiss();
            }

            handleNoResults();
        }
    }

    private void handleNoResults() {
        boolean hasFavorites = favouriteRecipes != null && favouriteRecipes.size() > 0;
        txtNoFavorite.setVisibility(hasFavorites ? View.GONE : View.VISIBLE);
    }

}
