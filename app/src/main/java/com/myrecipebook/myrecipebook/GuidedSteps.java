package com.myrecipebook.myrecipebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Thomas on 28/08/2017.
 */

public class GuidedSteps extends AppCompatActivity {

    List<String> steplist = new ArrayList<>();
    int currentStep;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guidedsteps);

        Intent intent = getIntent();
        steplist = intent.getStringArrayListExtra("stepArray");

        final TextView stepN = (TextView) findViewById(R.id.stepNumber);
        final TextView stepT = (TextView) findViewById(R.id.stepText);
        final Button prev = (Button) findViewById(R.id.previous);
        final Button next = (Button) findViewById(R.id.next);
        final Button repeat = (Button) findViewById(R.id.repeat);
        final Button exit = (Button) findViewById(R.id.exit);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.ITALIAN);
                }
            }
        });

        currentStep = 0;

        stepN.setText("Passo "+ (currentStep+1));
        stepT.setText(steplist.get(currentStep));


        reproduceText(tts,stepT.getText().toString());

        prev.setClickable(false);
        prev.setVisibility(View.INVISIBLE);
        prev.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(currentStep>0) {
                    currentStep--;
                    stepN.setText("Passo " + (currentStep+1));
                    stepT.setText(steplist.get(currentStep));
                    next.setClickable(true);
                    next.setVisibility(View.VISIBLE);
                    reproduceText(tts,stepT.getText().toString());
                } if (currentStep == 0){
                    prev.setClickable(false);
                    prev.setVisibility(View.INVISIBLE);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(currentStep<steplist.size()-1) {
                    currentStep++;
                    stepN.setText("Passo " + (currentStep+1));
                    stepT.setText(steplist.get(currentStep));
                    prev.setClickable(true);
                    prev.setVisibility(View.VISIBLE);
                    reproduceText(tts,stepT.getText().toString());
                } if (currentStep == steplist.size()-1){
                    next.setClickable(false);
                    next.setVisibility(View.INVISIBLE);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                reproduceText(tts,stepT.getText().toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void reproduceText(final TextToSpeech tts, final String s){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, 500);

    }
}
