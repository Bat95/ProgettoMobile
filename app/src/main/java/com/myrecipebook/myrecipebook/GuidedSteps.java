package com.myrecipebook.myrecipebook;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by Thomas on 28/08/2017.
 */

public class GuidedSteps extends AppCompatActivity {

    List<String> steplist = new ArrayList<>();
    int currentStep;

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
        final Button exit = (Button) findViewById(R.id.exit);

        currentStep = 0;

        stepN.setText("Passo "+ (currentStep+1));
        stepT.setText(steplist.get(currentStep));

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
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
