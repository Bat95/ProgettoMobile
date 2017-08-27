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

import java.util.ArrayList;
import java.util.List;


public class Suggestion extends Fragment {

    List<Recipe> NewRecipes;

    //ad esempio quante ricette vuoi ricevere
    int number = 6;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Ricette suggerite");

        // temporary! chiedo al server le news sulle nuove ricette aggiunte :) in pratica sono solo tot random
        /*HttpHelper.Get(
                        getActivity().getApplicationContext(),
                        "http://646ffb06.ngrok.io/api/values/number",
                        new BaseHttpResponseHandler<InfoDto>(InfoDto.class) {

                            @Override
                            public void handleResponse(InfoDto response) {
                                NewRecipes = new ArrayList<>(response.);
                            }

                            @Override
                            public void handleError(String errorMessage) {
                                super.handleError(errorMessage);
                            }
                        });*/
    }
}
