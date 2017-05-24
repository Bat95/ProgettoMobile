package com.myrecipebook.myrecipebook;


import android.os.Bundle;
import android.os.CountDownTimer;
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

        final TextView reverse = (TextView) view.findViewById(R.id.textView9);


        final CountDownTimer cd = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                reverse.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                reverse.setText("done!");
            }
        };

        final Button srev = (Button) view.findViewById(R.id.startreverse);
        srev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(srev.getText().equals("Start")){
                    cd.start();
                    srev.setText("Stop");
                } else if (srev.getText().equals("Stop")){
                    cd.cancel();
                    reverse.setText("seconds remaining: 0");
                    srev.setText("Start");
                }
            }


        });

    }
}
