package com.myrecipebook.myrecipebook;

/**
 * Created by Sonia on 12/05/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.myrecipebook.myrecipebook.utilities.Preferences;


public class Allergies extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.allergies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Gestione allergie");

        CheckedTextView latticini = (CheckedTextView) view.findViewById(R.id.Latticini);
        CheckedTextView frutta_a_guscio = (CheckedTextView) view.findViewById(R.id.Frutta_a_guscio);
        CheckedTextView crostacei = (CheckedTextView) view.findViewById(R.id.Crostacei);
        CheckedTextView uova = (CheckedTextView) view.findViewById(R.id.Uova);
        CheckedTextView soia = (CheckedTextView) view.findViewById(R.id.Soia);
        CheckedTextView frumento_e_glutine = (CheckedTextView) view.findViewById(R.id.Frumento_e_glutine);
        CheckedTextView pesce = (CheckedTextView) view.findViewById(R.id.Pesce);

        Context context = getContext();

        readAllergenePreference(context, latticini, "latticini");
        readAllergenePreference(context, frutta_a_guscio, "frutta");
        readAllergenePreference(context, crostacei, "crostacei");
        readAllergenePreference(context, uova, "uova");
        readAllergenePreference(context, soia, "soia");
        readAllergenePreference(context, frumento_e_glutine, "frumento");
        readAllergenePreference(context, pesce, "pesce");



        latticini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "latticini");
            }

        });

        frutta_a_guscio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "frutta");
            }

        });

        crostacei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "crostacei");
            }

        });

        uova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "uova");
            }

        });

        soia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "soia");
            }

        });

        frumento_e_glutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "frumento");
            }

        });

        pesce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, "pesce");
            }

        });
    }

    private void updateAllergenePreference(View view, String key) {
        CheckedTextView checkTxt = (CheckedTextView) view;
        boolean checked = checkTxt.isChecked();
        checkTxt.setChecked(!checked);
        Preferences.store(getContext(), key, checkTxt.isChecked());
    }

    private void readAllergenePreference(Context context, CheckedTextView checkTxt, String key) {
        checkTxt.setChecked(Preferences.get(context, key, boolean.class, false));
    }
}
