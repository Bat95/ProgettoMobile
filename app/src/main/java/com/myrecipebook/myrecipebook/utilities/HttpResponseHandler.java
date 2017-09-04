package com.myrecipebook.myrecipebook.utilities;

import java.lang.reflect.Type;

public abstract class HttpResponseHandler<T>
{
    private Type genericType;

    protected HttpResponseHandler(Type genericType) {
        this.genericType = genericType;
    }

    public Type getResponseType() {
        return genericType;
    }

    public abstract void handleResponse(T response);
    public abstract void handleError(String errorMessage);
}
