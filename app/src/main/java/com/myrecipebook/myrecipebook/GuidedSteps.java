package com.myrecipebook.myrecipebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Thomas on 28/08/2017.
 */

public class GuidedSteps extends AppCompatActivity implements DialogInterface {

    List<String> steplist = new ArrayList<>();
    int currentStep;
    TextToSpeech tts;
    Button prev;
    Button next;
    Button exit;
    Button repeat;
    TextView stepN;
    TextView stepT;
    AlertDialog alertDialog;
    boolean useSpeechRecognizer = true;
    private SpeechRecognizerManager mSpeechManager;
    private AudioManager myAudioManager;
    int currentVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guidedsteps);

        Intent intent = getIntent();
        steplist = intent.getStringArrayListExtra("stepArray");

        stepN = (TextView) findViewById(R.id.stepNumber);
        stepT = (TextView) findViewById(R.id.stepText);
        prev = (Button) findViewById(R.id.previous);
        next = (Button) findViewById(R.id.next);
        repeat = (Button) findViewById(R.id.repeat);
        exit = (Button) findViewById(R.id.exit);

        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        currentVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        try {
            // create alert dialog to make user choose if wants voice recognition
            alertDialog = new AlertDialog.Builder(GuidedSteps.this).create();
            //LayoutInflater inflater = getLayoutInflater();
            //View alertLayout = inflater.inflate(R.layout.alert_dialog, null);

            alertDialog.setMessage("Voice Recognition Use");
            alertDialog.setMessage("Vuoi utilizzare il riconoscimento vocale?");

            //alertDialog.setView(alertLayout);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"SI", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    useSpeechRecognizer = true;
                    BeginSteps();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    useSpeechRecognizer = false;
                    BeginSteps();
                }
            });

            // show it
            alertDialog.show();
        } catch(Exception e) {

        }

        currentStep = 0;

        stepN.setText("Passo "+ (currentStep+1));
        stepT.setText(steplist.get(currentStep));

            prev.setClickable(false);
            prev.setVisibility(View.INVISIBLE);
            prev.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (currentStep > 0) {
                        currentStep--;
                        stepN.setText("Passo " + (currentStep + 1));
                        stepT.setText(steplist.get(currentStep));
                        next.setClickable(true);
                        next.setVisibility(View.VISIBLE);
                        if (useSpeechRecognizer) {
                            reproduceText(tts, stepT.getText().toString());
                        }
                    }
                    if (currentStep == 0) {
                        prev.setClickable(false);
                        prev.setVisibility(View.INVISIBLE);
                    }
                }
            });

            next.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (currentStep < steplist.size() - 1) {
                        currentStep++;
                        stepN.setText("Passo " + (currentStep + 1));
                        stepT.setText(steplist.get(currentStep));
                        prev.setClickable(true);
                        prev.setVisibility(View.VISIBLE);
                        if (useSpeechRecognizer) {
                            reproduceText(tts, stepT.getText().toString());
                        }
                    }
                    if (currentStep == steplist.size() - 1) {
                        next.setClickable(false);
                        next.setVisibility(View.INVISIBLE);
                    }
                }
            });

            exit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (useSpeechRecognizer) {
                        mSpeechManager.destroy();
                        mSpeechManager = null;
                    }
                    onBackPressed();
                }
            });


            repeat.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    reproduceText(tts, stepT.getText().toString());
                }
            });
    }

    void BeginSteps() {
        if (useSpeechRecognizer) {

            if(PermissionHandler.checkPermission(this,PermissionHandler.RECORD_AUDIO)) {

                if(mSpeechManager==null)
                {
                    SetSpeechListener();
                }
                else if(!mSpeechManager.ismIsListening())
                {
                    mSpeechManager.destroy();
                    SetSpeechListener();
                }
            }
            else
            {
                PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO,this);
            }


            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.ITALIAN);
                    }
                    if (status == TextToSpeech.SUCCESS) {
                        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onDone(String utteranceId) {
                                if (mSpeechManager == null) {
                                    SetSpeechListener();
                                }
                            }

                            @Override
                            public void onError(String utteranceId) {
                            }

                            @Override
                            public void onStart(String utteranceId) {
                                if (mSpeechManager != null) {
                                    mSpeechManager.destroy();
                                }
                            }
                        });
                    }
                }
            });

            reproduceText(tts, stepT.getText().toString());
            if (myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)) {
                myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            } else {
                myAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }

        } else {

            repeat.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        if (useSpeechRecognizer && tts != null) {
            tts.shutdown();
            myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_SHOW_UI);
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (useSpeechRecognizer && tts != null) {
            tts.shutdown();
        }
        if(mSpeechManager!=null) {
            mSpeechManager.destroy();
            mSpeechManager=null;
        }
        super.onPause();
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
        }, 1000);
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode)
        {
            case PermissionHandler.RECORD_AUDIO:
                if(grantResults.length>0) {
                    if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                        SetSpeechListener();
                    }
                }
                break;

        }
    }

    private void SetSpeechListener()
    {
        mSpeechManager=new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {

                if(results!=null && results.size()>0)
                {

                    if(results.size()==1)
                    {
                        if (results.get(0).contains("avanti")) {
                            next.performClick();
                        }
                        if (results.get(0).contains("indietro")) {
                                prev.performClick();
                        }
                        if (results.get(0).contains("esci")) {
                            exit.performClick();
                        }
                        if (results.get(0).contains("ripeti")) {
                                        repeat.performClick();
                         }

                    }
                    else {
                        StringBuilder sb = new StringBuilder();
                        if (results.size() > 5) {
                            results = (ArrayList<String>) results.subList(0, 5);
                        }
                        for (String result : results) {
                            sb.append(result).append("\n");
                        }
                        sb.append(results.get(0));

                        if (results.get(0).contains("avanti")) {
                            next.performClick();
                        }
                        if (results.get(0).contains("indietro")) {
                            prev.performClick();
                        }
                        if (results.get(0).contains("esci")) {
                            exit.performClick();
                        }
                        if (results.get(0).contains("ripeti")) {
                            repeat.performClick();
                        }
                    }
                }

            }
        });
    }
}
