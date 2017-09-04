package com.myrecipebook.myrecipebook.utilities;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

/**
 * Created by Sonia
 */

public class HttpHelper {
    public static <T> void Get(Context context, String url, HttpResponseHandler<T> responseHandler) {
        SendRequest(context, Request.Method.GET, url, null, responseHandler);
    }

    public static <T> void Post(Context context, String url, Object body, HttpResponseHandler<T> responseHandler) {
        SendRequest(context, Request.Method.POST, url, body, responseHandler);
    }

    private static <T> void SendRequest(Context context, int method, String url, final Object requestBody, final HttpResponseHandler<T> responseHandler) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(
                    method,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            responseHandler.handleResponse((T) new Gson().fromJson(response, responseHandler.getResponseType()));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            responseHandler.handleError(error.getMessage());
                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        if(requestBody == null) {
                            return null;
                        }

                        String jsonBody = new Gson().toJson(requestBody);
                        return jsonBody.getBytes("utf-8");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }
    }
}