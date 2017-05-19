package com.myrecipebook.myrecipebook;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


public class Help extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Aiuto");

        final Chronometer cr = (Chronometer) view.findViewById(R.id.chronometer);

        final TextView tv = (TextView) view.findViewById(R.id.testTV);

        Button start = (Button) view.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.start();
            }
        });

        final Button stop = (Button) view.findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.stop();
                cr.setBase(SystemClock.elapsedRealtime()); //serve per resettare a 0
            }
        });

        Button test = (Button) view.findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cr.start();
                cr.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                    @Override
                    public void onChronometerTick(Chronometer chronometer) {
                        //Mettere le condizioni, per esempio a TOT time fare qualcosa
                        if (chronometer.getText().toString().equals("00:10")) {
                            tv.setText("Teminato!");
                            cr.stop();
                            cr.setText("00:00");
                            cr.setBase(SystemClock.elapsedRealtime());
                        }
                    }

                });
            }


        });
    }
}
