package com.naughtybitch.discogsclient;


import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DiscogsClient {

    private int status_code;

    private final String consumer_key = "zrNFOdbKoUvMXDxixdPY";
    private final String consumer_secret = "NgFRwbmvWCwmIiIRjAaiUnWSutmlHDNJ%26";
    private String request_token = "";
    private String request_token_secret = "";
    private String oauth_verifier = "";
    private String access_token = "";
    private String access_token_secret = "";
    private String callbackURL = "http://localhost:8000";

    private String api_base_url = "https://api.discogs.com";
    private String request_token_url = api_base_url + "/oauth/request_token";
    private String authorize_url = "https://discogs.com/oauth/authorize?oauth_token=";
    private String access_token_url = api_base_url + "/oauth/access_token";
    private String identity_url = api_base_url + "/oauth/identity";

    private static DiscogsClient instance;

    public static DiscogsClient getInstance() {
        if (instance == null) {
            synchronized (DiscogsClient.class) {
                if (instance == null) {
                    Log.i("instance", "Creating new instance");
                    instance = new DiscogsClient();
                }
            }
        }
        return instance;
    }

    public interface VolleyCallback {
        void onSuccess(String result);

        void onError(String result);
    }

    public interface VolleyCallback_JSON {
        void onSuccess(JSONObject object);

        void onError(String result);
    }

    private Timestamp currentTimeStamp() {
        // 1) create a java calendar instance
        Calendar calendar = Calendar.getInstance();
        // 2) get a java.util.Date from the calendar instance.
        //    this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();
        // 3) a java current time (now) instance
        final java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        return currentTimestamp;
    }

    public void requestToken(String client_id, String client_secret, final VolleyCallback volleyCallback) {
        Timestamp currentTimestamp = currentTimeStamp();
        final String auth = "OAuth oauth_consumer_key=\"" + client_id + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_signature=\"" + client_secret + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\"";
        Log.i("time_stamp", "Current timestamp " + currentTimestamp.getTime());
        StringRequest request_token = new StringRequest(Request.Method.POST, request_token_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
                Log.i("response", "Response is " + response);
                Log.i("status_code", "Status " + status_code);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.toString());
                Log.i("response", "Response is " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                headers.put("User-Agent", "Discogsnect/0.1 +http://discogsnect.com");
                Log.i("headers", "Header is " + headers);
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(request_token);
    }

    public void accessToken(String client_id, String client_secret, final VolleyCallback volleyCallback) {
        Timestamp currentTimestamp = currentTimeStamp();

        final String auth = "OAuth oauth_consumer_key=\"" + client_id + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + getRequest_token() + "\", " +
                "oauth_signature=\"" + client_secret + getRequest_token_secret() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_verifier=\"" + getOauth_verifier() + "\", " +
                "oauth_version=\"1.0\"";
        Log.i("time_stamp", "Current timestamp " + currentTimestamp.getTime());
        StringRequest access_token = new StringRequest(Request.Method.GET, access_token_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyCallback.onSuccess(response);
                Log.i("response", "Response is " + response);
                Log.i("status_code", "Status " + status_code);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.toString());
                Log.i("response", "Response is " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                headers.put("User-Agent", "Discogsnect/0.1 +http://discogsnect.com");
                Log.i("headers", "Header is " + headers);
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(access_token);
    }

    public void identityRequest(final VolleyCallback_JSON volleyCallback) {
        Timestamp currentTimestamp = currentTimeStamp();

        final String auth = "OAuth oauth_consumer_key=\"" + getConsumer_key() + "\", " +
                "oauth_nonce=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_token=\"" + getAccess_token() + "\", " +
                "oauth_signature=\"" + getConsumer_secret() + getAccess_token_secret() + "\", " +
                "oauth_signature_method=\"PLAINTEXT\", " +
                "oauth_timestamp=\"" + currentTimestamp.getTime() + "\", " +
                "oauth_version=\"1.0\"";
        Log.i("time_stamp", "Current timestamp " + currentTimestamp.getTime());
        JsonObjectRequest identity_request = new JsonObjectRequest(Request.Method.GET, identity_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                volleyCallback.onSuccess(response);
                Log.i("response", "Response is " + response);
                Log.i("status_code", "Status " + status_code);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallback.onError(error.toString());
                Log.i("response", "Response is " + error);
                Log.i("status_code", "Status " + status_code);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                headers.put("Authorization", auth);
                headers.put("User-Agent", "Discogsnect/0.1 +http://discogsnect.com");
                Log.i("headers", "Header is " + headers);
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                status_code = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(identity_request);
    }

    public String getOauth_verifier() {
        return oauth_verifier;
    }

    public void setOauth_verifier(String oauth_verifier) {
        this.oauth_verifier = oauth_verifier;
    }

    public String getAuthorize_url() {
        return authorize_url + getRequest_token();
    }

    public void setAuthorize_url(String authorize_url) {
        this.authorize_url = authorize_url;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getCallbackURL() {
        return callbackURL;
    }

    public void setCallbackURL(String callbackURL) {
        this.callbackURL = callbackURL;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_token_secret() {
        return access_token_secret;
    }

    public void setAccess_token_secret(String access_token_secret) {
        this.access_token_secret = access_token_secret;
    }

    public String getConsumer_key() {
        return consumer_key;
    }

    public String getConsumer_secret() {
        return consumer_secret;
    }

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }

    public String getRequest_token_secret() {
        return request_token_secret;
    }

    public void setRequest_token_secret(String request_token_secret) {
        this.request_token_secret = request_token_secret;
    }


}
