package com.myrecipebook.myrecipebook.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.myrecipebook.myrecipebook.utilities.BaseHttpResponseHandler;
import com.myrecipebook.myrecipebook.utilities.HttpHelper;
import com.myrecipebook.myrecipebook.R;
import com.myrecipebook.myrecipebook.models.Feedback;


public class HelpFragment extends Fragment {

    private EditText _txtEmail;
    private EditText _txtMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Aiuto");

        _txtEmail = (EditText) view.findViewById(R.id.txtEmail);
        _txtMessage = (EditText) view.findViewById(R.id.txtMessage);

        FloatingActionButton btnSend = (FloatingActionButton) view.findViewById(R.id.btnSendFeedback);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback(_txtEmail.getText().toString(), _txtMessage.getText().toString());
            }
        });
    }

    private void sendFeedback(String email, String message) {

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Per inviare un feedback è necessario specificare una mail", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Per inviare un feedback è necessario specificare un messaggio", Toast.LENGTH_SHORT).show();
            return;
        }

        Feedback feedback = new Feedback();
        feedback.email = email;
        feedback.message = message;

        try {
            HttpHelper.Post(
                    getActivity().getApplicationContext(),
                    getString(R.string.serverIp)+"feedback",
                    feedback,
                    new BaseHttpResponseHandler<String>(String.class) {

                        @Override
                        public void handleResponse(String response) {
                            Toast.makeText(getContext(), "Grazie per il feedback!", Toast.LENGTH_SHORT).show();

                            _txtMessage.setText("");
                        }

                        @Override
                        public void handleError(String errorMessage) {
                            Toast.makeText(getContext(), "Errore durante l'invio del feedback, per favore riprova", Toast.LENGTH_SHORT).show();
                            super.handleError(errorMessage);
                        }
                    });

        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Errore durante l'invio del feedback, per favore riprova", Toast.LENGTH_SHORT).show();
        }
    }
}
