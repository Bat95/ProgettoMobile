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
import android.widget.CheckedTextView;


public class Allergies extends Fragment {

    boolean latticini_value;
    boolean frutta_a_guscio_value;
    boolean crostacei_value;
    boolean uova_value;
    boolean soia_value;
    boolean frumento_e_glutine_value;
    boolean pesce_value;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.allergies, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Gestione allergie");


        final CheckedTextView latticini = (CheckedTextView) view.findViewById(R.id.Latticini);
        final CheckedTextView frutta_a_guscio = (CheckedTextView) view.findViewById(R.id.Frutta_a_guscio);
        final CheckedTextView crostacei = (CheckedTextView) view.findViewById(R.id.Crostacei);
        final CheckedTextView uova = (CheckedTextView) view.findViewById(R.id.Uova);
        final CheckedTextView soia = (CheckedTextView) view.findViewById(R.id.Soia);
        final CheckedTextView frumento_e_glutine = (CheckedTextView) view.findViewById(R.id.Frumento_e_glutine);
        final CheckedTextView pesce = (CheckedTextView) view.findViewById(R.id.Pesce);


        latticini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latticini.isChecked()) {
                    latticini.setChecked(false);
                    latticini_value = false;
                }
                else {
                    latticini.setChecked(true);
                    latticini_value = true;
                }
            }

        });

        frutta_a_guscio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frutta_a_guscio.isChecked()) {
                    frutta_a_guscio.setChecked(false);
                    frutta_a_guscio_value = false;
                }
                else {
                    frutta_a_guscio.setChecked(true);
                    frutta_a_guscio_value = true;
                }
            }

        });

        crostacei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (crostacei.isChecked()) {
                    crostacei.setChecked(false);
                    crostacei_value = false;
                }
                else {
                    crostacei.setChecked(true);
                    crostacei_value = true;
                }
            }

        });

        uova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uova.isChecked()) {
                    uova.setChecked(false);
                    uova_value = false;
                }
                else {
                    uova.setChecked(true);
                    uova_value = true;
                }
            }

        });

        soia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soia.isChecked()) {
                    soia.setChecked(false);
                    soia_value = false;
                }
                else {
                    soia.setChecked(true);
                    soia_value = true;
                }
            }

        });

        frumento_e_glutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frumento_e_glutine.isChecked()) {
                    frumento_e_glutine.setChecked(false);
                    frumento_e_glutine_value = false;
                }
                else {
                    frumento_e_glutine.setChecked(true);
                    frumento_e_glutine_value = true;
                }
            }

        });

        pesce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pesce.isChecked()) {
                    pesce.setChecked(false);
                    pesce_value = false;
                }
                else {
                    pesce.setChecked(true);
                    pesce_value = true;
                }
            }

        });
    }
}
