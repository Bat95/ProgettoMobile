package com.myrecipebook.myrecipebook;

import java.lang.reflect.Type;

public class BaseHttpResponseHandler<T> extends HttpResponseHandler<T> {

    public  BaseHttpResponseHandler(Type genericType)
    {
        super(genericType);
    }

    @Override
    public void handleResponse(T response) {
    }

    @Override
    public void handleError(String errorMessage) {
    }
}
