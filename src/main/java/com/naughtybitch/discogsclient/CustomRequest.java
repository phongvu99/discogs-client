package com.naughtybitch.discogsclient;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Create a class which extend the Volley Request<T> class
 **/
public class CustomRequest extends Request {
    private Response.Listener listener;
    /**
     * Declare a Gson variable which will be used to convert the json response
     * to POJO
     **/
    private Gson gson;
    /**
     * Declare a Class variable. This Class will represent the POJO.Basically
     * this variable will hold the instance of the java object which you
     * want as response
     **/
    private Class responseClass;

    /**
     ** Constructor for your custom class
     *  @param Request Method- GET,POST
     *  @param Request URL
     *  @param Java Object which you want as response
     *  @param Response listener to notify success response
     *  @param Error listener to notify error response **/
    public <T> CustomRequest(int method, String url, Class responseClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        gson = new Gson();
        this.listener = listener;
        this.responseClass = responseClass;
    }

    /**
     * This method needs to be implemented to parse the raw network response
     * and return an appropriate response type.This method will be called
     * from a worker thread. The response
     * will not be delivered if you return null.
     *
     *  @param Network Response- Response payload as byte[],headers and status code
     **/

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            /**
             First you will have to convert the NetworkResponse into a jsonstring.
             Then that json string can be converted into the required java object
             using Gson
             **/
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
            return Response.success(gson.fromJson(jsonString, responseClass), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    /**
     * This is called on the main thread with the object you returned in
     * parseNetworkResponse(). You should be invoking your callback interface
     * from here
     **/
    @Override
    protected void deliverResponse(Object response) {

        listener.onResponse(response);
    }
}