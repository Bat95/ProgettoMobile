package com.myrecipebook.myrecipebook.fragments;

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

import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.utilities.Preferences;


public class AllergiesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.allergies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.allergies_title));

        CheckedTextView latticini = (CheckedTextView) view.findViewById(R.id.Latticini);
        CheckedTextView frutta_a_guscio = (CheckedTextView) view.findViewById(R.id.Frutta_a_guscio);
        CheckedTextView crostacei = (CheckedTextView) view.findViewById(R.id.Crostacei);
        CheckedTextView uova = (CheckedTextView) view.findViewById(R.id.Uova);
        CheckedTextView soia = (CheckedTextView) view.findViewById(R.id.Soia);
        CheckedTextView frumento_e_glutine = (CheckedTextView) view.findViewById(R.id.Frumento_e_glutine);
        CheckedTextView pesce = (CheckedTextView) view.findViewById(R.id.Pesce);

        Context context = getContext();

        readAllergenePreference(context, latticini, getString(R.string.latticini_prefKey));
        readAllergenePreference(context, frutta_a_guscio, getString(R.string.frutta_a_guscio_prefKey));
        readAllergenePreference(context, crostacei, getString(R.string.crostacei_prefKey));
        readAllergenePreference(context, uova, getString(R.string.uova_prefKey));
        readAllergenePreference(context, soia, getString(R.string.soia_prefKey));
        readAllergenePreference(context, frumento_e_glutine, getString(R.string.frumento_e_glutine_prefKey));
        readAllergenePreference(context, pesce, getString(R.string.pesce_prefKey));



        latticini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.latticini_prefKey));
            }

        });

        frutta_a_guscio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.frutta_a_guscio_prefKey));
            }

        });

        crostacei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.crostacei_prefKey));
            }

        });

        uova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.uova_prefKey));
            }

        });

        soia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.soia_prefKey));
            }

        });

        frumento_e_glutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.frumento_e_glutine_prefKey));
            }

        });

        pesce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAllergenePreference(v, getString(R.string.pesce_prefKey));
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
