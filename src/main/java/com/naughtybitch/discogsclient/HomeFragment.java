package com.naughtybitch.discogsclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public void buttonOnClickListener(View v) {
        Button send_request = v.findViewById(R.id.send_request);
        send_request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.send_request):
                Toast.makeText(getActivity(), "Sending request", Toast.LENGTH_SHORT).show();
                sendRequest();
        }
    }

    public void sendRequest() {
        // 1) create a java calendar instance
        Calendar calendar = Calendar.getInstance();

        // 2) get a java.util.Date from the calendar instance.
        //    this date will represent the current instant, or "now".
        java.util.Date now = calendar.getTime();

        // 3) a java current time (now) instance
        final java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        Log.i("time_stamp", currentTimestamp.toString());

        String tag_string_request = "string_request";
        String tag_json_object = "json_object_request";
        final TextView textView = (TextView) getActivity().findViewById(R.id.response);
        final ImageView coverImage = (ImageView) getActivity().findViewById(R.id.album_cover);

        // Instantiate the RequestQueue
        String api_base_url = "https://api.discogs.com";
        String search_url = api_base_url + "/database/search?release_title=everyday+life&artist=coldplay&per_page=3&page=1";
        CustomRequest customRequest = new CustomRequest(Request.Method.GET, search_url, SearchResponse.class, new Response.Listener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse response) {
                // Display the first 500 characters of the response string.
                String tag_image_request = "image_request";
                Log.i("response", "Response is " + response);
                List<Result> list = response.getResults();
                String coverImage_url = list.get(0).getCoverImage();
                textView.setText("CoverImage url: " + coverImage_url + "\nHave a look at this amazing album cover by Coldplay!");
                Glide.with(getActivity()).load(coverImage_url).fitCenter().placeholder(R.drawable.bigpump).crossFade().into(coverImage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
                textView.setText("Response is: " + error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type:", "application/x-www-form-urlencoded");
//                headers.put("Authorization:",
//                        "OAuth oauth_consumer_key=\"zrNFOdbKoUvMXDxixdPY\", " +
//                                "oauth_nonce=\"" + currentTimestamp.toString() + "\", " +
//                                "oauth_signature=\"NgFRwbmvWCwmIiIRjAaiUnWSutmlHDNJ&\", " +
//                                "oauth_signature_method=\"PLAINTEXT\", " +
//                                "oauth_timestamp=\"" + currentTimestamp.toString() + "\", " +
//                                "oauth_callback=\"none\"");
//                headers.put("User-Agent:", "Discogsnect/0.1 +http://discogsnect.com");
                headers.put("Authorization", "Discogs key=zrNFOdbKoUvMXDxixdPY" + "," + "secret=NgFRwbmvWCwmIiIRjAaiUnWSutmlHDNJ");
                return headers;
            }
        };
//        // Request a string response from the provide URL.
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, search_url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Display the first 500 characters of the response string.
//                        Log.i("response", "Response is " + response);
//                        textView.setText("Response is: " + response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
//                textView.setText("Response is: " + error);
//            }
//
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
////                headers.put("Content-Type:", "application/x-www-form-urlencoded");
////                headers.put("Authorization:",
////                        "OAuth oauth_consumer_key=\"zrNFOdbKoUvMXDxixdPY\", " +
////                                "oauth_nonce=\"" + currentTimestamp.toString() + "\", " +
////                                "oauth_signature=\"NgFRwbmvWCwmIiIRjAaiUnWSutmlHDNJ&\", " +
////                                "oauth_signature_method=\"PLAINTEXT\", " +
////                                "oauth_timestamp=\"" + currentTimestamp.toString() + "\", " +
////                                "oauth_callback=\"none\"");
////                headers.put("User-Agent:", "Discogsnect/0.1 +http://discogsnect.com");
//                headers.put("Authorization", "Discogs key=zrNFOdbKoUvMXDxixdPY" + "," + "secret=NgFRwbmvWCwmIiIRjAaiUnWSutmlHDNJ");
//                return headers;
//            }
//        };
        // Add the request to the RequestQueue
        AppController.getInstance().addToRequestQueue(customRequest, tag_json_object);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        buttonOnClickListener(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
